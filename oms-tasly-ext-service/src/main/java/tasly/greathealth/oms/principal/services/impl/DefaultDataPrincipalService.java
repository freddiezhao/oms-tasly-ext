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
package tasly.greathealth.oms.principal.services.impl;

import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.principal.dto.DataPrincipal;
import tasly.greathealth.oms.domain.principal.DataPrincipalData;
import tasly.greathealth.oms.principal.services.DataPrincipalService;


/**
 * @author Henter Liu
 */
@SuppressWarnings("deprecation")
public class DefaultDataPrincipalService extends AbstractHybrisService implements DataPrincipalService
{
	@Override
	@Transactional
	public List<DataPrincipalData> batchUpdate(final List<DataPrincipal> dataPrincipals)
	{
		final List<DataPrincipalData> result = new LinkedList<DataPrincipalData>();
		final Map<String, DataPrincipalData> oldMap = new HashMap<String, DataPrincipalData>();
		final Map<String, DataPrincipal> newMap = new HashMap<String, DataPrincipal>();
		final List<DataPrincipalData> removeList = new LinkedList<DataPrincipalData>();
		final List<DataPrincipalData> oldPrincipals = getPersistenceManager().createCriteriaQuery(DataPrincipalData.class)
				.resultList();
		for (final DataPrincipal dataPrincipal : dataPrincipals)
		{
			final String newUniqueid = dataPrincipal.getUniqueid();
			newMap.put(newUniqueid, dataPrincipal);
		}
		for (final DataPrincipalData dataPrincipalData : oldPrincipals)
		{
			final String oldUniqueid = dataPrincipalData.getUniqueid();
			oldMap.put(oldUniqueid, dataPrincipalData);
			if (!newMap.containsKey(oldUniqueid))
			{
				removeList.add(oldMap.get(oldUniqueid));
			}
		}
		for (final DataPrincipal dataPrincipal : dataPrincipals)
		{
			final String uniqueid = dataPrincipal.getUniqueid();
			if (!oldMap.containsKey(uniqueid))
			{
				// created
				final DataPrincipalData newDataPrincipalData = getPersistenceManager().create(DataPrincipalData.class);
				newDataPrincipalData.setUniqueid(dataPrincipal.getUniqueid());
				newDataPrincipalData.setIsGroup(dataPrincipal.getIsGroup());
				newDataPrincipalData.setGroup(dataPrincipal.getGroup());
				newDataPrincipalData.setActive(dataPrincipal.getActive());
				result.add(newDataPrincipalData);
			}
			else
			{
				// updated
				final DataPrincipalData oldDataPrincipalData = oldMap.get(uniqueid);
				oldDataPrincipalData.setIsGroup(dataPrincipal.getIsGroup());
				oldDataPrincipalData.setGroup(dataPrincipal.getGroup());
				oldDataPrincipalData.setActive(dataPrincipal.getActive());
				result.add(oldDataPrincipalData);
			}
		}
		for (final DataPrincipalData dataPrincipalData : removeList)
		{
			// removed
			getPersistenceManager().remove(dataPrincipalData);
		}
		getPersistenceManager().flush();
		return result;
	}

	@Override
	@Transactional
	public List<DataPrincipalData> getAllUsers()
	{
		return super.getPersistenceManager().createCriteriaQuery(DataPrincipalData.class)
				.where(Restrictions.eq(DataPrincipalData.ISGROUP, false)).resultList();
	}

	@Override
	@Transactional
	public List<DataPrincipalData> getAllUserGroups()
	{
		return super.getPersistenceManager().createCriteriaQuery(DataPrincipalData.class)
				.where(Restrictions.eq(DataPrincipalData.ISGROUP, true)).resultList();
	}

	@Override
	@Transactional
	public DataPrincipalData getPrincipalByUid(final String uid)
	{
		try
		{
			return getPersistenceManager().getByIndex(DataPrincipalData.UX_DATAPRINCIPALS_UNIQUEID, uid);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Unique Id is not correct, " + uid, e);
		}
	}

	@Override
	@Transactional
	public DataPrincipalData updatePrincipal(final DataPrincipal dataPrincipal)
	{
		final DataPrincipalData data = getPrincipalByUid(dataPrincipal.getUniqueid());
		data.setIsGroup(dataPrincipal.getIsGroup());
		data.setGroup(dataPrincipal.getGroup());
		data.setPrincipal(dataPrincipal.getPrincipal());
		data.setActive(dataPrincipal.getActive());
		return data;
	}
}
