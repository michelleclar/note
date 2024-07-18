package org.carl;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.CreateCollectionReq;
import io.milvus.v2.service.collection.request.GetLoadStateReq;

public class Demo {
    public static void main(String[] args) {


        String CLUSTER_ENDPOINT = "http://localhost:19530";

// 1. Connect to Milvus server
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(CLUSTER_ENDPOINT)
                .username("root")
                .password("Milvus")
                .build();

        MilvusClientV2 client = new MilvusClientV2(connectConfig);

// 2. Create a collection in quick setup mode
        CreateCollectionReq quickSetupReq = CreateCollectionReq.builder()
                .collectionName("quick_setup")
                .dimension(5)
                .build();

        client.createCollection(quickSetupReq);

// Thread.sleep(5000);

        GetLoadStateReq quickSetupLoadStateReq = GetLoadStateReq.builder()
                .collectionName("quick_setup")
                .build();

        Boolean res = client.getLoadState(quickSetupLoadStateReq);

        System.out.println(res);
    }
}
