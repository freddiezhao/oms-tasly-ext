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
 *
 *
 */
package tasly.greathealth.oms.api.principal;

import java.util.List;

import tasly.greathealth.oms.api.principal.dto.DataPrincipal;


/**
 * @author Henter Liu
 */
public interface DataPrincipalFacade
{
	List<DataPrincipal> batchUpdate(List<DataPrincipal> dataPrincipals);

	List<DataPrincipal> getAllUsers();

	List<DataPrincipal> getAllUserGroups();

	DataPrincipal getPrincipalByUid(String uid);

	DataPrincipal updatePrincipal(DataPrincipal dataPrincipal);
}
