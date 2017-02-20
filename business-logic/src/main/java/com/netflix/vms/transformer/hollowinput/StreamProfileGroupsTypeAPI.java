package com.netflix.vms.transformer.hollowinput;

import com.netflix.hollow.api.custom.HollowObjectTypeAPI;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;

@SuppressWarnings("all")
public class StreamProfileGroupsTypeAPI extends HollowObjectTypeAPI {

    private final StreamProfileGroupsDelegateLookupImpl delegateLookupImpl;

    StreamProfileGroupsTypeAPI(VMSHollowInputAPI api, HollowObjectTypeDataAccess typeDataAccess) {
        super(api, typeDataAccess, new String[] {
            "groupName",
            "streamProfileIds"
        });
        this.delegateLookupImpl = new StreamProfileGroupsDelegateLookupImpl(this);
    }

    public int getGroupNameOrdinal(int ordinal) {
        if(fieldIndex[0] == -1)
            return missingDataHandler().handleReferencedOrdinal("StreamProfileGroups", ordinal, "groupName");
        return getTypeDataAccess().readOrdinal(ordinal, fieldIndex[0]);
    }

    public StringTypeAPI getGroupNameTypeAPI() {
        return getAPI().getStringTypeAPI();
    }

    public int getStreamProfileIdsOrdinal(int ordinal) {
        if(fieldIndex[1] == -1)
            return missingDataHandler().handleReferencedOrdinal("StreamProfileGroups", ordinal, "streamProfileIds");
        return getTypeDataAccess().readOrdinal(ordinal, fieldIndex[1]);
    }

    public StreamProfileIdListTypeAPI getStreamProfileIdsTypeAPI() {
        return getAPI().getStreamProfileIdListTypeAPI();
    }

    public StreamProfileGroupsDelegateLookupImpl getDelegateLookupImpl() {
        return delegateLookupImpl;
    }

    @Override
    public VMSHollowInputAPI getAPI() {
        return (VMSHollowInputAPI) api;
    }

}