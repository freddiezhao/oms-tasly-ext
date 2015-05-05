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
package tasly.greathealth.tmall.inventory.file.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import tasly.greathealth.tmall.inventory.file.WriteToFile;


/**
 *
 */
public class WriteToFileImpl implements WriteToFile
{

	@SuppressWarnings("null")
	@Override
	public void createFile(final String path, final String fileName, final StringBuilder content) throws Exception
	{

		final Calendar cal = Calendar.getInstance();
		final String current = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);

		FileOutputStream fos = null;
		PrintWriter pw = null;

		final File file = new File(path + fileName + "-" + current + ".log");
		final StringBuilder buf = new StringBuilder();
		buf.append(content);

		fos = new FileOutputStream(file);
		pw = new PrintWriter(fos);
		pw.write(buf.toString().toCharArray());
		pw.flush();
		if (pw != null)
		{
			pw.close();
		}
		if (fos != null)
		{
			fos.close();
		}
	}
}
