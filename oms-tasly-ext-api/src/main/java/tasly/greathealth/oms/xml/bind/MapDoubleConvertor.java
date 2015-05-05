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
package tasly.greathealth.oms.xml.bind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "MapDoubleConvertor")
@XmlAccessorType(XmlAccessType.FIELD)
public class MapDoubleConvertor
{

	private final List<MapEntry> entries = new ArrayList<MapDoubleConvertor.MapEntry>();

	public void addEntry(final MapEntry entry)
	{
		entries.add(entry);
	}

	public List<MapEntry> getEntries()
	{
		return entries;
	}

	// 结构依赖于需要Conver的Map，比如这里Map<String, Person>
	public static class MapEntry
	{
		private String key;
		private Double value;

		public MapEntry()
		{

		}

		public MapEntry(final String key, final Double person)
		{
			this.key = key;
			this.value = person;
		}

		public MapEntry(final Map.Entry<String, Double> entry)
		{
			this.key = entry.getKey();
			this.value = entry.getValue();
		}

		public String getKey()
		{
			return key;
		}

		public void setKey(final String key)
		{
			this.key = key;
		}

		public Double getValue()
		{
			return value;
		}

		public void setValue(final Double value)
		{
			this.value = value;
		}

	}

}
