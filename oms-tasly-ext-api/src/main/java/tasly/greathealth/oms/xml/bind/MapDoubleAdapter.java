package tasly.greathealth.oms.xml.bind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import tasly.greathealth.oms.xml.bind.MapDoubleConvertor.MapEntry;


public class MapDoubleAdapter extends XmlAdapter<MapDoubleConvertor, Map<String, Double>>
{

	@Override
	public Map<String, Double> unmarshal(final MapDoubleConvertor convertor) throws Exception
	{
		final List<MapEntry> entries = convertor.getEntries();
		if (entries != null && entries.size() > 0)
		{
			final Map<String, Double> map = new HashMap<String, Double>();
			for (final MapEntry mapEntry : entries)
			{
				map.put(mapEntry.getKey(), mapEntry.getValue());
			}
			return map;
		}
		return null;
	}

	@Override
	public MapDoubleConvertor marshal(final Map<String, Double> map) throws Exception
	{
		final MapDoubleConvertor convertor = new MapDoubleConvertor();
		for (final Map.Entry<String, Double> entry : map.entrySet())
		{
			convertor.addEntry(new MapDoubleConvertor.MapEntry(entry));
		}
		return convertor;
	}

}
