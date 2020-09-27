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

package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.metadata.MetadataBinder;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MetadataServiceTest {

    @Test
    void deleteMetadataTest() {
        final Workspace workspace = Workspace.builder()
                .id("test-id")
                .name("test-name")
                .build();

        final MetadataBinder metadataBinderMock = Mockito.mock(MetadataBinder.class);
        final DataStoragePairRepository dataStoragePairRepositoryMock = Mockito.mock(DataStoragePairRepository.class);
        doNothing().when(dataStoragePairRepositoryMock).removeByWorkspace(workspace);
        final Map<Workspace, Metadata> cacheMock = Mockito.mock(Map.class);
        when(cacheMock.remove(workspace)).thenReturn(null);


        final MetadataService metadataService = new MetadataService(metadataBinderMock, dataStoragePairRepositoryMock, cacheMock);
        metadataService.deleteMetadata(workspace);

        verifyNoInteractions(metadataBinderMock);

        verify(dataStoragePairRepositoryMock, times(1)).removeByWorkspace(workspace);
        verifyNoMoreInteractions(dataStoragePairRepositoryMock);

        verify(cacheMock, times(1)).remove(workspace);
        verifyNoMoreInteractions(cacheMock);
    }
}
