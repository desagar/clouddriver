/*
 * Copyright (c) 2017, 2018, Oracle Corporation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the Apache License Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * If a copy of the Apache License Version 2.0 was not distributed with this file,
 * You can obtain one at https://www.apache.org/licenses/LICENSE-2.0.html
 */
package com.netflix.spinnaker.clouddriver.oracle.deploy.validator

import com.netflix.spinnaker.clouddriver.deploy.DescriptionValidator
import com.netflix.spinnaker.clouddriver.oracle.OracleOperation
import com.netflix.spinnaker.clouddriver.oracle.deploy.description.ResizeOracleServerGroupDescription
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperations
import org.springframework.stereotype.Component
import org.springframework.validation.Errors

@OracleOperation(AtomicOperations.RESIZE_SERVER_GROUP)
@Component("resizeOracleServerGroupDescriptionValidator")
class ResizeOracleServerGroupDescriptionValidator extends DescriptionValidator<ResizeOracleServerGroupDescription> {

  @Override
  void validate(List priorDescriptions, ResizeOracleServerGroupDescription description, Errors errors) {
    def helper = new StandardOracleAttributeValidator("resizeServerGroupDescription", errors)

    helper.validateNotEmptyString(description.serverGroupName, "serverGroupName")
    helper.validateNotEmptyString(description.region, "region")
    helper.validateNotEmptyString(description.accountName, "accountName")
    //TODO: check serviceLimits?
    Integer targetSize = description.targetSize?: (description.capacity?.desired?:0)
    helper.validateNonNegative(targetSize?:0, "targetSize")
    if (description.capacity) {
      helper.validateCapacity(description.capacity.min, description.capacity.max, description.capacity.desired)
    }
  }
}
