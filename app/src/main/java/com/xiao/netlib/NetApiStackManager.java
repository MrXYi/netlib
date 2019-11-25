package com.xiao.netlib;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2019/11/25.
 */
public class NetApiStackManager {

    private static NetApiStackManager sInstance;
    private List<NetApi> mNetApis = new ArrayList<NetApi>();

    private NetApiStackManager() {
    }

    public static NetApiStackManager getInstance() {
        if (null == sInstance) {
            sInstance = new NetApiStackManager();
        }
        return sInstance;
    }

    public void addNetApi(NetApi netApi) {
        if (null != netApi) {
            mNetApis.add(netApi);
        }
    }

    public void removeNetApi(NetApi netApi) {
        if (null != netApi) {
            netApi.cancel();
            mNetApis.remove(netApi);
        }
    }

    public void removeAllNetApis() {
        for (int i = mNetApis.size() - 1; i >= 0; i--) {
            NetApi netApi = mNetApis.get(i);
            try {
                if (netApi != null) {
                    netApi.cancel();
                    mNetApis.remove(netApi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mNetApis.clear();
    }
}

