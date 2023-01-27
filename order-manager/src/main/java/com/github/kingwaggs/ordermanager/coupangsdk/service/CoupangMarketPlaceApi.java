package com.github.kingwaggs.ordermanager.coupangsdk.service;

import com.github.kingwaggs.ordermanager.coupangsdk.config.Configuration;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.*;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.CancelProductDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundOrder;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.cancel.RefundDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.PrepareShipmentDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.shipping.UploadInvoiceDto;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.dto.sold.CoupangOrders;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.product.OpenApiResultOfOSellerProduct;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import com.google.common.reflect.TypeToken;
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

    public CoupangOrders getOrderSheetsByVendorId(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Integer maxPerPage) throws ApiException {
        ApiResponse<CoupangOrders> resp = this.getOrderSheetsByVendorIdWithHttpInfo(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, null, maxPerPage, null);
        return resp.getData();
    }

    public ApiResponse<CoupangOrders> getOrderSheetsByVendorIdWithHttpInfo(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, String nextToken, Integer maxPerPage, String searchType) throws ApiException {
        Call call = this.getOrderSheetsByVendorIdValidateBeforeCall(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, nextToken, maxPerPage, searchType, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangOrders>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call getOrderSheetsByVendorIdValidateBeforeCall(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, String nextToken, Integer maxPerPage, String searchType, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getOrderSheetsByVendorId(Async)");
        } else {
            Call call = this.getOrderSheetsByVendorIdCall(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, nextToken, maxPerPage, searchType, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call getOrderSheetsByVendorIdCall(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, String nextToken, Integer maxPerPage, String searchType, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/ordersheets".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        if (createdAtFrom != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "createdAtFrom", createdAtFrom));
        }

        if (createdAtTo != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "createdAtTo", createdAtTo));
        }

        if (status != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "status", status));
        }

        if (nextToken != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "nextToken", nextToken));
        }

        if (maxPerPage != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "maxPerPage", maxPerPage));
        }

        if (searchType != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "searchType", searchType));
        }

        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"*/*"};
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

    public CoupangOrders getOrderByOrderId(Long orderId, String vendorId, String xRequestedBy) throws ApiException {
        ApiResponse<CoupangOrders> resp = this.getOrderSheetByOrderIdWithHttpInfo(orderId, vendorId, xRequestedBy);
        return (CoupangOrders)resp.getData();
    }

    public ApiResponse<CoupangOrders> getOrderSheetByOrderIdWithHttpInfo(Long orderId, String vendorId, String xRequestedBy) throws ApiException {
        Call call = this.getOrderSheetByOrderIdValidateBeforeCall(orderId, vendorId, xRequestedBy, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<CoupangOrders>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call getOrderSheetByOrderIdCall(Long orderId, String vendorId, String xRequestedBy, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/{orderId}/ordersheets".replaceAll("\\{format\\}", "json").replaceAll("\\{orderId\\}", this.apiClient.escapeString(orderId.toString())).replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"*/*"};
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

    private Call getOrderSheetByOrderIdValidateBeforeCall(Long orderId, String vendorId, String xRequestedBy, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (orderId == null) {
            throw new ApiException("Missing the required parameter 'orderId' when calling getOrderSheetByOrderId(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getOrderSheetByOrderId(Async)");
        } else {
            Call call = this.getOrderSheetByOrderIdCall(orderId, vendorId, xRequestedBy, progressListener, progressRequestListener);
            return call;
        }
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

    public PrepareShipmentDto.Response applyPrepareShipmentStatus(PrepareShipmentDto.Request prepareShipmentReqDto, String vendorId, String xRequestedBy) throws ApiException {
        ApiResponse<PrepareShipmentDto.Response> resp = this.applyPrepareShipmentStatusWithHttpInfo(prepareShipmentReqDto, vendorId, xRequestedBy);
        return (PrepareShipmentDto.Response)resp.getData();
    }

    public ApiResponse<PrepareShipmentDto.Response> applyPrepareShipmentStatusWithHttpInfo(PrepareShipmentDto.Request prepareShipmentReqDto, String vendorId, String xRequestedBy) throws ApiException {
        Call call = this.applyPrepareShipmentStatusValidateBeforeCall(prepareShipmentReqDto, vendorId, xRequestedBy, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<PrepareShipmentDto.Response>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call applyPrepareShipmentStatusValidateBeforeCall(PrepareShipmentDto.Request prepareShipmentReqDto, String vendorId, String xRequestedBy, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (prepareShipmentReqDto == null) {
            throw new ApiException("Missing the required parameter 'prepareShipmentReqDto' when calling applyPrepareShipmentStatus(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling applyPrepareShipmentStatus(Async)");
        } else {
            Call call = this.applyPrepareShipmentStatusCall(prepareShipmentReqDto, vendorId, xRequestedBy, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call applyPrepareShipmentStatusCall(PrepareShipmentDto.Request prepareShipmentReqDto, String vendorId, String xRequestedBy, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/ordersheets/acknowledgement".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"*/*"};
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
        this.apiClient.setApiKey(this.GenerateToken("PATCH", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, prepareShipmentReqDto, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    public PrepareShipmentDto.Response applyInvoicesUpload(UploadInvoiceDto.Request uploadInvoiceReqDtoV2, String vendorId, String xRequestedBy) throws ApiException {
        ApiResponse<PrepareShipmentDto.Response> resp = this.applyInvoicesUploadWithHttpInfo(uploadInvoiceReqDtoV2, vendorId, xRequestedBy);
        return (PrepareShipmentDto.Response)resp.getData();
    }

    public ApiResponse<PrepareShipmentDto.Response> applyInvoicesUploadWithHttpInfo(UploadInvoiceDto.Request uploadInvoiceReqDtoV2, String vendorId, String xRequestedBy) throws ApiException {
        Call call = this.applyInvoicesUploadValidateBeforeCall(uploadInvoiceReqDtoV2, vendorId, xRequestedBy, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<PrepareShipmentDto.Response>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call applyInvoicesUploadValidateBeforeCall(UploadInvoiceDto.Request uploadInvoiceReqDtoV2, String vendorId, String xRequestedBy, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (uploadInvoiceReqDtoV2 == null) {
            throw new ApiException("Missing the required parameter 'uploadInvoiceReqDtoV2' when calling applyInvoicesUpload(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling applyInvoicesUpload(Async)");
        } else {
            Call call = this.applyInvoicesUploadCall(uploadInvoiceReqDtoV2, vendorId, xRequestedBy, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call applyInvoicesUploadCall(UploadInvoiceDto.Request uploadInvoiceReqDtoV2, String vendorId, String xRequestedBy, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/orders/invoices".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[]{"*/*"};
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
        return this.apiClient.buildCall(localVarPath, "POST", localVarQueryParams, uploadInvoiceReqDtoV2, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    public OpenApiResultOfOSellerProduct readSellerProduct(String xRequestedBy, String sellerProductId) throws ApiException {
        ApiResponse<OpenApiResultOfOSellerProduct> resp = this.readSellerProductWithHttpInfo(xRequestedBy, sellerProductId);
        return (OpenApiResultOfOSellerProduct)resp.getData();
    }

    public ApiResponse<OpenApiResultOfOSellerProduct> readSellerProductWithHttpInfo(String xRequestedBy, String sellerProductId) throws ApiException {
        Call call = this.readSellerProductValidateBeforeCall(xRequestedBy, sellerProductId, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResultOfOSellerProduct>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call readSellerProductValidateBeforeCall(String xRequestedBy, String sellerProductId, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling readSellerProduct(Async)");
        } else if (sellerProductId == null) {
            throw new ApiException("Missing the required parameter 'sellerProductId' when calling readSellerProduct(Async)");
        } else {
            Call call = this.readSellerProductCall(xRequestedBy, sellerProductId, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call readSellerProductCall(String xRequestedBy, String sellerProductId, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}".replaceAll("\\{format\\}", "json").replaceAll("\\{sellerProductId\\}", this.apiClient.escapeString(sellerProductId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
        if (xRequestedBy != null) {
            localVarHeaderParams.put("X-Requested-By", this.apiClient.parameterToString(xRequestedBy));
        }

        Map<String, Object> localVarFormParams = new HashMap();
        String[] localVarAccepts = new String[0];
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

    public RefundOrder getRefundClaimOrdersByVendorId(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Integer maxPerPage, String cancelType) throws ApiException {
        ApiResponse<RefundOrder> resp = this.getReturnRequestedListByVendorIdWithHttpInfo(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, null, null, maxPerPage, null, cancelType);
        return (RefundOrder)resp.getData();
    }

    public RefundOrder getCancelRequestedListByVendorId(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Long orderId, Integer maxPerPage, String cancelType) throws ApiException {
        ApiResponse<RefundOrder> resp = this.getReturnRequestedListByVendorIdWithHttpInfo(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, orderId, null, maxPerPage, null, cancelType);
        return (RefundOrder)resp.getData();
    }

    public ApiResponse<RefundOrder> getReturnRequestedListByVendorIdWithHttpInfo(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Long orderId, String nextToken, Integer maxPerPage, String searchType, String cancelType) throws ApiException {
        Call call = this.getReturnRequestedListByVendorIdValidateBeforeCall(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, orderId, nextToken, maxPerPage, searchType, cancelType, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<RefundOrder>() {}).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call getReturnRequestedListByVendorIdValidateBeforeCall(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Long orderId, String nextToken, Integer maxPerPage, String searchType, String cancelType, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getReturnRequestedListByVendorId(Async)");
        } else {
            return this.getReturnRequestedListByVendorIdCall(vendorId, xRequestedBy, createdAtFrom, createdAtTo, status, orderId, nextToken, maxPerPage, searchType, cancelType, progressListener, progressRequestListener);
        }
    }

    private Call getReturnRequestedListByVendorIdCall(String vendorId, String xRequestedBy, String createdAtFrom, String createdAtTo, String status, Long orderId, String nextToken, Integer maxPerPage, String searchType, String cancelType, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/returnRequests".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        if (createdAtFrom != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "createdAtFrom", createdAtFrom));
        }

        if (createdAtTo != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "createdAtTo", createdAtTo));
        }

        if (status != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "status", status));
        }

        if (orderId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "orderId", orderId));
        }

        if (nextToken != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "nextToken", nextToken));
        }

        if (maxPerPage != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "maxPerPage", maxPerPage));
        }

        if (searchType != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "searchType", searchType));
        }

        if (cancelType != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "cancelType", cancelType));
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

    public RefundDto.Response doStopRelease(Long cancelId, String vendorId, RefundDto.Request refundRequest, String xRequestedBy) throws ApiException {
        ApiResponse<RefundDto.Response> resp = this.doStopReleaseWithHttpInfo(cancelId, vendorId, refundRequest, xRequestedBy);
        return (RefundDto.Response)resp.getData();
    }

    private ApiResponse<RefundDto.Response> doStopReleaseWithHttpInfo(Long receiptId, String vendorId, RefundDto.Request releaseStopDto, String xRequestedBy) throws ApiException {
        Call call = this.doStopReleaseValidateBeforeCall(receiptId, vendorId, releaseStopDto, xRequestedBy, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<RefundDto.Response>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call doStopReleaseValidateBeforeCall(Long receiptId, String vendorId, RefundDto.Request releaseStopDto, String xRequestedBy, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (receiptId == null) {
            throw new ApiException("Missing the required parameter 'receiptId' when calling doStopRelease(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling doStopRelease(Async)");
        } else if (releaseStopDto == null) {
            throw new ApiException("Missing the required parameter 'releaseStopDto' when calling doStopRelease(Async)");
        } else {
            Call call = this.doStopReleaseCall(receiptId, vendorId, releaseStopDto, xRequestedBy, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call doStopReleaseCall(Long receiptId, String vendorId, RefundDto.Request releaseStopDto, String xRequestedBy, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/returnRequests/{receiptId}/stoppedShipment".replaceAll("\\{format\\}", "json").replaceAll("\\{receiptId\\}", this.apiClient.escapeString(receiptId.toString())).replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
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
        this.apiClient.setApiKey(this.GenerateToken("PATCH", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, releaseStopDto, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    public CancelProductDto.Response cancelOrder(String vendorId, Long orderId, CancelProductDto.Request cancelRequest) throws ApiException {
        ApiResponse<CancelProductDto.Response> resp = this.cancelOrderV5WithHttpInfo(vendorId, orderId, cancelRequest);
        return (CancelProductDto.Response)resp.getData();
    }

    public ApiResponse<CancelProductDto.Response> cancelOrderV5WithHttpInfo(String vendorId, Long orderId, CancelProductDto.Request cancelRequestDtoV5) throws ApiException {
        Call call = this.cancelOrderV5ValidateBeforeCall(vendorId, orderId, cancelRequestDtoV5, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<CancelProductDto.Response>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call cancelOrderV5ValidateBeforeCall(String vendorId, Long orderId, CancelProductDto.Request cancelRequestDtoV5, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling cancelOrderV5(Async)");
        } else if (orderId == null) {
            throw new ApiException("Missing the required parameter 'orderId' when calling cancelOrderV5(Async)");
        } else if (cancelRequestDtoV5 == null) {
            throw new ApiException("Missing the required parameter 'cancelRequestDtoV5' when calling cancelOrderV5(Async)");
        } else {
            Call call = this.cancelOrderV5Call(vendorId, orderId, cancelRequestDtoV5, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call cancelOrderV5Call(String vendorId, Long orderId, CancelProductDto.Request cancelRequestDtoV5, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v5/vendors/{vendorId}/orders/{orderId}/cancel".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString())).replaceAll("\\{orderId\\}", this.apiClient.escapeString(orderId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        Map<String, String> localVarHeaderParams = new HashMap();
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
        return this.apiClient.buildCall(localVarPath, "POST", localVarQueryParams, cancelRequestDtoV5, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    public RefundDto.Response grantRefundClaim(Long receiptId, RefundDto.Request returnApprovalDto, String vendorId, String xRequestedBy) throws ApiException {
        ApiResponse<RefundDto.Response> resp = this.confirmCancelByPartnerByReceiptIdWithHttpInfo(receiptId, returnApprovalDto, vendorId, xRequestedBy);
        return (RefundDto.Response)resp.getData();
    }

    public ApiResponse<RefundDto.Response> confirmCancelByPartnerByReceiptIdWithHttpInfo(Long receiptId, RefundDto.Request returnApprovalDto, String vendorId, String xRequestedBy) throws ApiException {
        Call call = this.confirmCancelByPartnerByReceiptIdValidateBeforeCall(receiptId, returnApprovalDto, vendorId, xRequestedBy, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<RefundDto.Response>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call confirmCancelByPartnerByReceiptIdValidateBeforeCall(Long receiptId, RefundDto.Request returnApprovalDto, String vendorId, String xRequestedBy, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (receiptId == null) {
            throw new ApiException("Missing the required parameter 'receiptId' when calling confirmCancelByPartnerByReceiptId(Async)");
        } else if (returnApprovalDto == null) {
            throw new ApiException("Missing the required parameter 'returnApprovalDto' when calling confirmCancelByPartnerByReceiptId(Async)");
        } else if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling confirmCancelByPartnerByReceiptId(Async)");
        } else {
            Call call = this.confirmCancelByPartnerByReceiptIdCall(receiptId, returnApprovalDto, vendorId, xRequestedBy, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call confirmCancelByPartnerByReceiptIdCall(Long receiptId, RefundDto.Request returnApprovalDto, String vendorId, String xRequestedBy, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/returnRequests/{receiptId}/approval".replaceAll("\\{format\\}", "json").replaceAll("\\{receiptId\\}", this.apiClient.escapeString(receiptId.toString())).replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
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
        this.apiClient.setApiKey(this.GenerateToken("PATCH", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, returnApprovalDto, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

}
