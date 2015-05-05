/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.service.serialization;

import com.hybris.commons.tenant.TenantContextService;
import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.exceptions.TypeCodeNotFoundException;
import com.hybris.kernel.persistence.EntityRecord;
import com.hybris.kernel.persistence.exporting.CollectionData;
import com.hybris.kernel.persistence.exporting.CsvSerializationService;
import com.hybris.kernel.persistence.exporting.DeserializationCallback;
import com.hybris.kernel.persistence.exporting.ManyToManyRelationData;
import com.hybris.kernel.persistence.exporting.MapData;
import com.hybris.kernel.persistence.exporting.SerializationException;
import com.hybris.kernel.persistence.serialization.impl.DefaultCsvSerializationService;
import com.hybris.kernel.typesystem.TypeSystem;
import com.hybris.kernel.typesystem.model.CollectionAttribute;
import com.hybris.kernel.typesystem.model.ManagedObjectType;
import com.hybris.kernel.typesystem.model.MapAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.csvreader.CsvReader;


/**
 * created by i313879
 */
public class TaslyCsvSerializationService implements CsvSerializationService
{


	private CsvSerializationService csvSerializationService;

	/**
	 * @return the csvSerializationService
	 */
	public CsvSerializationService getCsvSerializationService()
	{
		return csvSerializationService;
	}

	/**
	 * @param csvSerializationService the csvSerializationService to set
	 */
	public void setCsvSerializationService(final CsvSerializationService csvSerializationService)
	{
		this.csvSerializationService = csvSerializationService;
	}

	@Override
	public void serialize(final String paramString, final EntityRecord paramEntityRecord, final boolean paramBoolean,
			final Writer paramWriter)
	{
		// YTODO Auto-generated method stub
		csvSerializationService.serialize(paramString, paramEntityRecord, paramBoolean, paramWriter);
	}

	@Override
	public void deserialize(final DeserializationCallback<EntityRecord> paramDeserializationCallback, final String typeCode,
			final InputStream inputStream, final Map<String, String> directives) throws IOException
	{
		// YTODO Auto-generated method stub
		ManagedObjectType managedObject;
		if (!(csvSerializationService instanceof DefaultCsvSerializationService))
		{
			csvSerializationService.deserialize(paramDeserializationCallback, typeCode, inputStream, directives);
			return;
		}
		final DefaultCsvSerializationService defaultCsvSerializationService = (DefaultCsvSerializationService) csvSerializationService;
		try
		{
			final Field typeSystem = DefaultCsvSerializationService.class.getDeclaredField("typeSystem");
			typeSystem.setAccessible(true);
			managedObject = ((TypeSystem) typeSystem.get(defaultCsvSerializationService)).getManagedObjectType(typeCode);
			typeSystem.setAccessible(false);
		}
		catch (final TypeCodeNotFoundException e)
		{
			throw new SerializationException("Cannot deserialize for type " + typeCode + " as type is unknown", e);
		}
		catch (final Exception e)
		{
			throw new RuntimeException("runtime excption occurs" + e);
		}
		final CsvReader reader = new CsvReader(inputStream, Charset.forName("UTF-8"));
		try
		{
			final boolean hasHeaders = reader.readHeaders();
			if (!(hasHeaders))
			{
				throw new SerializationException("You must provide a header in csv file");
			}

			while (reader.readRecord())
			{
				paramDeserializationCallback.handleDeserializedData(deserializeSingleCsvLine(defaultCsvSerializationService,
						managedObject, reader, directives));
			}

		}
		catch (final IOException ex)
		{
			throw ex;
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			reader.close();
		}


	}

	private EntityRecord deserializeSingleCsvLine(final DefaultCsvSerializationService defaultCsvSerializationService,
			final ManagedObjectType managedObject, final CsvReader reader, final Map<String, String> directives) throws Exception
	{

		// Reflections
		final Class theClass = defaultCsvSerializationService.getClass();
		final Method deserializeLocalizedAttribute = theClass.getDeclaredMethod("deserializeLocalizedAttribute",
				ManagedObjectType.class, CsvReader.class, Map.class, String.class, Map.class);
		final Method deserializeValueTypeAttribute = theClass.getDeclaredMethod("deserializeValueTypeAttribute",
				ManagedObjectType.class, CsvReader.class, Map.class, String.class, Map.class);
		final Method deserializeSimpleAttribute = theClass.getDeclaredMethod("deserializeSimpleAttribute", ManagedObjectType.class,
				CsvReader.class, Map.class, String.class, Map.class);

		final Method deserializeValueTypes = theClass.getDeclaredMethod("deserializeValueTypes", String.class, Map.class);
		final Field tenantContextService = theClass.getDeclaredField("tenantContextService");
		// Set accessibility
		tenantContextService.setAccessible(true);
		deserializeLocalizedAttribute.setAccessible(true);
		deserializeValueTypeAttribute.setAccessible(true);
		deserializeSimpleAttribute.setAccessible(true);
		deserializeValueTypes.setAccessible(true);
		// old logic
		final Map values = new HashMap();
		for (final String columnHeader : reader.getHeaders())
		{
			if (columnHeader.contains("_") && !isExpessType(managedObject))// do localization excepts the our 2 Express
				// classes
			{
				deserializeLocalizedAttribute.invoke(defaultCsvSerializationService, managedObject, reader, values, columnHeader,
						directives);
			}
			else if (columnHeader.contains("."))
			{
				deserializeValueTypeAttribute.invoke(defaultCsvSerializationService, managedObject, reader, values, columnHeader,
						directives);
			}
			else
			{
				deserializeSimpleAttribute.invoke(defaultCsvSerializationService, managedObject, reader, values, columnHeader,
						directives);
			}
		}

		deserializeValueTypes.invoke(defaultCsvSerializationService, managedObject.getTypeCode(), values);

		// set accessibility back to original
		deserializeLocalizedAttribute.setAccessible(true);
		deserializeValueTypeAttribute.setAccessible(true);
		deserializeSimpleAttribute.setAccessible(true);
		deserializeValueTypes.setAccessible(true);
		try
		{
			return new EntityRecord(
					((TenantContextService) tenantContextService.get(defaultCsvSerializationService)).getCurrentTenant(),
					managedObject.getTypeCode(), values);
		}
		finally
		{// set accessibility back to original
			tenantContextService.setAccessible(false);
		}
	}

	private boolean isExpessType(final ManagedObjectType managedObject)
	{
		final String typeCode = managedObject.getTypeCode();
		if (typeCode.equals("ExpressItemsData") || typeCode.equals("ExpresslocationsData"))
		{
			return true;
		}
		return false;
	}

	@Override
	public void serializeMany2ManyRelation(final String paramString1, final String paramString2,
			final ManyToManyRelationData paramManyToManyRelationData, final boolean paramBoolean, final Writer paramWriter)
	{
		// YTODO Auto-generated method stub
		csvSerializationService.serializeMany2ManyRelation(paramString1, paramString2, paramManyToManyRelationData, paramBoolean,
				paramWriter);
	}

	@Override
	public void deserializeMany2ManyRelation(final DeserializationCallback<ManyToManyRelationData> paramDeserializationCallback,
			final String paramString1, final String paramString2, final String paramString3, final InputStream paramInputStream)
			throws IOException
	{
		// YTODO Auto-generated method stub
		csvSerializationService.deserializeMany2ManyRelation(paramDeserializationCallback, paramString1, paramString2,
				paramString3, paramInputStream);
	}

	@Override
	public <K, V> void serializeMap(final HybrisId paramHybrisId, final Map<K, V> paramMap, final MapAttribute paramMapAttribute,
			final boolean paramBoolean, final Writer paramWriter)
	{
		// YTODO Auto-generated method stub
		csvSerializationService.serializeMap(paramHybrisId, paramMap, paramMapAttribute, paramBoolean, paramWriter);
	}

	@Override
	public <K, V> void deserializeMap(final DeserializationCallback<MapData<K, V>> paramDeserializationCallback,
			final MapAttribute paramMapAttribute, final InputStream paramInputStream, final Map<String, String> paramMap)
			throws IOException
	{
		// YTODO Auto-generated method stub
		csvSerializationService.deserializeMap(paramDeserializationCallback, paramMapAttribute, paramInputStream, paramMap);
	}

	@Override
	public void serializeCollection(final CollectionData paramCollectionData, final CollectionAttribute paramCollectionAttribute,
			final boolean paramBoolean, final Writer paramWriter)
	{
		// YTODO Auto-generated method stub
		csvSerializationService.serializeCollection(paramCollectionData, paramCollectionAttribute, paramBoolean, paramWriter);
	}

	@Override
	public void deserializeCollection(final DeserializationCallback<CollectionData> paramDeserializationCallback,
			final CollectionAttribute paramCollectionAttribute, final Class<?> paramClass, final InputStream paramInputStream,
			final Map<String, String> paramMap) throws IOException
	{
		// YTODO Auto-generated method stub
		csvSerializationService.deserializeCollection(paramDeserializationCallback, paramCollectionAttribute, paramClass,
				paramInputStream, paramMap);
	}


}
