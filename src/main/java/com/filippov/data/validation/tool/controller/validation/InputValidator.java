/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.controller.validation;

import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

public class InputValidator {

    private boolean validateWorkspaceId;
    private boolean validateWorkspaceDto;
    private boolean validateTablePairId;
    private boolean validateColumnPairId;
    private boolean validateCacheRequestDto;
    private boolean validateOffset;
    private boolean validateLimit;

    private String workspaceId;
    private WorkspaceDto workspaceDto;
    private String tablePairId;
    private String columnPairId;
    private CacheRequestDto cacheRequestDto;
    private Integer offset;
    private Integer limit;

    private InputValidator() {
    }

    public static InputValidator builder() {
        return new InputValidator();
    }

    public InputValidator withWorkspaceId(String workspaceId) {
        this.validateWorkspaceId = true;
        this.workspaceId = workspaceId;
        return this;
    }

    public InputValidator withWorkspaceDto(WorkspaceDto workspaceDto) {
        this.validateWorkspaceDto = true;
        this.workspaceDto = workspaceDto;
        return this;
    }

    public InputValidator withTablePairId(String tablePairId) {
        this.validateTablePairId = true;
        this.tablePairId = tablePairId;
        return this;
    }

    public InputValidator withColumnPairId(String columnPairId) {
        this.validateColumnPairId = true;
        this.columnPairId = columnPairId;
        return this;
    }

    public InputValidator withCacheRequestDto(CacheRequestDto cacheRequestDto) {
        this.validateCacheRequestDto = true;
        this.cacheRequestDto = cacheRequestDto;
        return this;
    }

    public InputValidator withOffset(Integer offset) {
        this.validateOffset = true;
        this.offset = offset;
        return this;
    }

    public InputValidator withLimit(Integer limit) {
        this.validateLimit = true;
        this.limit = limit;
        return this;
    }

    public void validate() {
        final StringBuilder sb = new StringBuilder();
        if (validateWorkspaceId
                && (workspaceId == null || StringUtils.isEmpty(workspaceId))) {
            sb.append("workspaceId is incorrect");
        }

        if (validateWorkspaceDto) {
            if (workspaceDto == null) {
                append(sb, "workspace is null");
            } else {
                if (StringUtils.isEmpty(workspaceDto.getName())) {
                    append(sb, "workspace name is incorrect");
                }

                if (workspaceDto.getLeft() == null) {
                    append(sb, "left datasource definition is null");
                } else {
                    if (workspaceDto.getLeft().getDatasourceType() == null) {
                        append(sb, "incorrect left datasource type");
                    }
                    if (workspaceDto.getLeft().getMaxConnections() == null || workspaceDto.getLeft().getMaxConnections() <= 0) {
                        append(sb, "incorrect left datasource max connections property");
                    }
                }

                if (workspaceDto.getRight() == null) {
                    append(sb, "right datasource definition is null");
                } else {
                    if (workspaceDto.getRight().getDatasourceType() == null) {
                        append(sb, "incorrect right datasource type");
                    }
                    if (workspaceDto.getRight().getMaxConnections() == null || workspaceDto.getRight().getMaxConnections() <= 0) {
                        append(sb, "incorrect right datasource max connections property");
                    }
                }
            }
        }

        if (validateTablePairId
                && (tablePairId == null || StringUtils.isEmpty(tablePairId))) {
            append(sb, "incorrect table pair id");
        }

        if (validateColumnPairId
                && (columnPairId == null || StringUtils.isEmpty(columnPairId))) {
            append(sb, "incorrect column pair id");
        }

        if (validateCacheRequestDto) {
            if (cacheRequestDto == null) {
                append(sb, "cache request is null");
            } else if (cacheRequestDto.getCacheFetchingCommand() == null) {
                append(sb, "incorrect caching command in cache request");
            }
        }

        if (validateOffset
                && (offset == null || offset < 0)) {
            append(sb, "incorrect offset");
        }

        if (validateLimit
                && (limit == null || limit < 0)) {
            append(sb, "incorrect limit");
        }

        if (sb.length() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect input: " + sb.toString());
        }
    }

    private void append(StringBuilder sb, String message) {
        if (sb.length() != 0) {
            sb.append("; ");
        }
        sb.append(message);
    }
}
