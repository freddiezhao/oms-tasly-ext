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
package tasly.greathealth.oms.principal.services;

import java.util.List;

import tasly.greathealth.oms.api.principal.dto.DataPrincipal;
import tasly.greathealth.oms.domain.principal.DataPrincipalData;


/**
 * @author Henter Liu
 */
public interface DataPrincipalService
{
	public List<DataPrincipalData> batchUpdate(List<DataPrincipal> dataPrincipals);

	public List<DataPrincipalData> getAllUsers();

	public List<DataPrincipalData> getAllUserGroups();

	public DataPrincipalData getPrincipalByUid(String uid);

	public DataPrincipalData updatePrincipal(DataPrincipal dataPrincipal);
}
