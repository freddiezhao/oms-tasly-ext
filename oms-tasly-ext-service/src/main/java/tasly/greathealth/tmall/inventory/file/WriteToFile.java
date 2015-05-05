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
package tasly.greathealth.tmall.inventory.file;



/**
 *
 */
public interface WriteToFile
{
	/**
	 * create file
	 *
	 * @param path
	 * @param fileName
	 */
	public void createFile(String path, String fileName, StringBuilder content) throws Exception;
}
