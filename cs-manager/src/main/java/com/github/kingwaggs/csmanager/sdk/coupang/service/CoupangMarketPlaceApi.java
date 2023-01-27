package com.github.kingwaggs.csmanager.sdk.coupang.service;

import com.github.kingwaggs.csmanager.sdk.coupang.config.Configuration;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.*;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiryAnswer;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangResponse;
import com.github.kingwaggs.csmanager.sdk.coupang.domain.dto.CoupangInquiries;
import com.github.kingwaggs.csmanager.sdk.coupang.exception.ApiException;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class CoupangMarketPlaceApi {

    private final CoupangMarketPlaceApiClient apiClient;
    private String secretKey;
    private String accessKey;

    public CoupangMarketPlaceApi() {
        this(Configuration.getDefaultApiClient());
    }

    public CoupangMarketPlaceApi(String secretKey, String accessKey) {
        this(secretKey, accessKey, false);
    }

    public CoupangMarketPlaceApi(String secretKey, String accessKey, boolean isDebugging) {
        this(Configuration.getDefaultApiClient());
        this.secretKey = secretKey;
        this.accessKey = accessKey;
    }

    public CoupangMarketPlaceApi(CoupangMarketPlaceApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CoupangInquiries getCustomerInquiries(String vendorId, String inquiryStartAt, String inquiryEndAt, String answeredType, Integer pageSize) throws ApiException {
        ApiResponse<CoupangInquiries> resp = this.getCustomerInquiriesByVendorIdWithHttpInfo(vendorId, vendorId, inquiryStartAt, inquiryEndAt, answeredType, null, null, pageSize);
        return resp.getData();
    }

    public CoupangResponse addAnswerToInquiry(String inquiryId, String vendorId, CoupangInquiryAnswer inquiryAnswer) throws ApiException {
        ApiResponse<CoupangResponse> resp = this.addCommentToInquiryWithHttpInfo(inquiryId, vendorId, vendorId, inquiryAnswer);
        return resp.getData();
    }

    private ApiResponse<CoupangResponse> addCommentToInquiryWithHttpInfo(String inquiryId, String vendorId, String xRequestedBy, CoupangInquiryAnswer inquiryAnswer) throws ApiException {
        Call call = this.addCommentToInquiryValidateBeforeCall(inquiryId, vendorId, xRequestedBy, inquiryAnswer, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangResponse>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private ApiResponse<CoupangInquiries> getCustomerInquiriesByVendorIdWithHttpInfo(String vendorId, String xRequestedBy, String inquiryStartAt, String inquiryEndAt, String answeredType, Long vendorItemId, Integer pageNum, Integer pageSize) throws ApiException {
        Call call = this.getCustomerInquiriesByVendorIdValidateBeforeCall(vendorId, xRequestedBy, inquiryStartAt, inquiryEndAt, answeredType, vendorItemId, pageNum, pageSize, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangInquiries>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call getCustomerInquiriesByVendorIdValidateBeforeCall(String vendorId, String xRequestedBy, String inquiryStartAt, String inquiryEndAt, String answeredType, Long vendorItemId, Integer pageNum, Integer pageSize, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getCustomerInquiriesByVendorId(Async)");
        } else {
            Call call = this.getCustomerInquiriesByVendorIdCall(vendorId, xRequestedBy, inquiryStartAt, inquiryEndAt, answeredType, vendorItemId, pageNum, pageSize, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call getCustomerInquiriesByVendorIdCall(String vendorId, String xRequestedBy, String inquiryStartAt, String inquiryEndAt, String answeredType, Long vendorItemId, Integer pageNum, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/onlineInquiries".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        if (inquiryStartAt != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "inquiryStartAt", inquiryStartAt));
        }

        if (inquiryEndAt != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "inquiryEndAt", inquiryEndAt));
        }

        if (answeredType != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "answeredType", answeredType));
        }

        if (vendorItemId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "vendorItemId", vendorItemId));
        }

        if (pageNum != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "pageNum", pageNum));
        }

        if (pageSize != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "pageSize", pageSize));
        }

        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "*/*"};
        String localVarAccept = this.apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[]{"application/json"};
        String localVarContentType = this.apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        if (progressListener != null) {
            this.apiClient.getHttpClient().networkInterceptors().add(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"api_key"};
        this.apiClient.setApiKey(this.GenerateToken("GET", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call addCommentToInquiryValidateBeforeCall(String inquiryId, String vendorId, String xRequestedBy, CoupangInquiryAnswer inquiryAnswer, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (inquiryId == null) {
            throw new ApiException("Missing the required parameter 'inquiryId' when calling addCommentToInquiry(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling addCommentToInquiry(Async)");
        } else {
            Call call = this.addCommentToInquiryCall(inquiryId, vendorId, xRequestedBy, inquiryAnswer, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call addCommentToInquiryCall(String inquiryId, String vendorId, String xRequestedBy, CoupangInquiryAnswer inquiryAnswer, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/onlineInquiries/{inquiryId}/replies".replaceAll("\\{format\\}", "json").replaceAll("\\{inquiryId\\}", this.apiClient.escapeString(inquiryId.toString())).replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"application/json", "*/*"};
        String localVarAccept = this.apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        String[] localVarContentTypes = new String[]{"application/json"};
        String localVarContentType = this.apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);
        if (progressListener != null) {
            this.apiClient.getHttpClient().networkInterceptors().add(new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
                }
            });
        }

        String[] localVarAuthNames = new String[]{"api_key"};
        this.apiClient.setApiKey(this.GenerateToken("POST", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "POST", localVarQueryParams, inquiryAnswer, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private String generatePath(String localVarPath, List<Pair> localQueryParams) {
        String path = localVarPath;
        int index = 1;
        Iterator var5 = localQueryParams.iterator();

        while(var5.hasNext()) {
            Pair entry = (Pair)var5.next();
            ++index;
            path = path + this.apiClient.escapeString(entry.getName()) + "=" + this.apiClient.escapeString(entry.getValue());
            if (index <= localQueryParams.size()) {
                path = path + "&";
            }
        }

        return path;
    }

    private String GenerateToken(String method, String path, String secretKey, String accessKey) {
        String authorization = "";

        try {
            authorization = Hmac.generate(method, path, secretKey, accessKey);
        } catch (Exception var7) {
            System.err.println("Exception when generating the HMAC token! " + var7);
        }

        return authorization;
    }

}
