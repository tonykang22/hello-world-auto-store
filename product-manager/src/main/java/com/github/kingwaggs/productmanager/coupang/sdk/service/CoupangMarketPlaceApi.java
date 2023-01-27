package com.github.kingwaggs.productmanager.coupang.sdk.service;

import com.github.kingwaggs.productmanager.coupang.sdk.config.Configuration;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.*;
import com.github.kingwaggs.productmanager.coupang.sdk.domain.product.*;
import com.github.kingwaggs.productmanager.coupang.sdk.exception.ApiException;
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

    public OpenApiResultOflong createSellerProduct(String xRequestedBy, OSellerProduct oSellerProduct) throws ApiException {
        ApiResponse<OpenApiResultOflong> resp = this.createSellerProductWithHttpInfo(xRequestedBy, oSellerProduct);
        return (OpenApiResultOflong)resp.getData();
    }

    public ApiResponse<OpenApiResultOflong> createSellerProductWithHttpInfo(String xRequestedBy, OSellerProduct oSellerProduct) throws ApiException {
        Call call = this.createSellerProductValidateBeforeCall(xRequestedBy, oSellerProduct, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResultOflong>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
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

    public OpenApiResult deleteSellerProduct(String xRequestedBy, String sellerProductId) throws ApiException {
        ApiResponse<OpenApiResult> resp = this.deleteSellerProductWithHttpInfo(xRequestedBy, sellerProductId);
        return (OpenApiResult)resp.getData();
    }

    public ApiResponse<OpenApiResult> deleteSellerProductWithHttpInfo(String xRequestedBy, String sellerProductId) throws ApiException {
        Call call = this.deleteSellerProductValidateBeforeCall(xRequestedBy, sellerProductId, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResult>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }


    public OpenApiResult stopSelling(String xRequestedBy, Long vendorItemId) throws ApiException {
        ApiResponse<OpenApiResult> resp = this.stopSellingWithHttpInfo(xRequestedBy, vendorItemId);
        return (OpenApiResult)resp.getData();
    }

    public ApiResponse<OpenApiResult> stopSellingWithHttpInfo(String xRequestedBy, Long vendorItemId) throws ApiException {
        Call call = this.stopSellingValidateBeforeCall(xRequestedBy, vendorItemId, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResult>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public ResponseDtoOfAutoCategorizationResponseDto doProductCategorizationUsingPOST(String xRequestedBy, AutoCategorizationRequestDto autoCategorizationRequestDto) throws ApiException {
        ApiResponse<ResponseDtoOfAutoCategorizationResponseDto> resp = this.doProductCategorizationUsingPOSTWithHttpInfo(xRequestedBy, autoCategorizationRequestDto);
        return (ResponseDtoOfAutoCategorizationResponseDto)resp.getData();
    }

    public ApiResponse<ResponseDtoOfAutoCategorizationResponseDto> doProductCategorizationUsingPOSTWithHttpInfo(String xRequestedBy, AutoCategorizationRequestDto autoCategorizationRequestDto) throws ApiException {
        Call call = this.doProductCategorizationUsingPOSTValidateBeforeCall(xRequestedBy, autoCategorizationRequestDto, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<ResponseDtoOfAutoCategorizationResponseDto>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public OpenApiResultOfOCategoryMeta getCategoryRelatedMetaByDisplayCategoryCode(String xRequestedBy, String displayCategoryCode) throws ApiException {
        ApiResponse<OpenApiResultOfOCategoryMeta> resp = this.getCategoryRelatedMetaByDisplayCategoryCodeWithHttpInfo(xRequestedBy, displayCategoryCode);
        return (OpenApiResultOfOCategoryMeta)resp.getData();
    }

    public ApiResponse<OpenApiResultOfOCategoryMeta> getCategoryRelatedMetaByDisplayCategoryCodeWithHttpInfo(String xRequestedBy, String displayCategoryCode) throws ApiException {
        Call call = this.getCategoryRelatedMetaByDisplayCategoryCodeValidateBeforeCall(xRequestedBy, displayCategoryCode, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResultOfOCategoryMeta>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto getReturnShippingCenter(String vendorId, String xRequestedBy, Integer pageNum, Integer pageSize) throws ApiException {
        ApiResponse<ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto> resp = this.getReturnShippingCenterWithHttpInfo(vendorId, xRequestedBy, pageNum, pageSize);
        return (ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto)resp.getData();
    }

    public ApiResponse<ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto> getReturnShippingCenterWithHttpInfo(String vendorId, String xRequestedBy, Integer pageNum, Integer pageSize) throws ApiException {
        Call call = this.getReturnShippingCenterValidateBeforeCall(vendorId, xRequestedBy, pageNum, pageSize, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<ResponseDtoOfPagedListOfShippingPlaceResponseReturnDto>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public PagedResponseOfOutboundInquiryReturn getOutboundShippingPlace(String vendorId, Integer pageNum, Integer pageSize, List<Long> placeCodes, List<String> placeNames) throws ApiException {
        ApiResponse<PagedResponseOfOutboundInquiryReturn> resp = this.getOutboundShippingPlaceWithHttpInfo(vendorId, pageNum, pageSize, placeCodes, placeNames);
        return (PagedResponseOfOutboundInquiryReturn)resp.getData();
    }

    public ApiResponse<PagedResponseOfOutboundInquiryReturn> getOutboundShippingPlaceWithHttpInfo(String vendorId, Integer pageNum, Integer pageSize, List<Long> placeCodes, List<String> placeNames) throws ApiException {
        Call call = this.getOutboundShippingPlaceValidateBeforeCall(vendorId, pageNum, pageSize, placeCodes, placeNames, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<PagedResponseOfOutboundInquiryReturn>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public OpenApiPagedResultOfOSellerProductStatusHistory readSellerProductHistory(String xRequestedBy, String sellerProductId) throws ApiException {
        ApiResponse<OpenApiPagedResultOfOSellerProductStatusHistory> resp = this.readSellerProductHistoryWithHttpInfo(xRequestedBy, sellerProductId);
        return (OpenApiPagedResultOfOSellerProductStatusHistory)resp.getData();
    }

    public ApiResponse<OpenApiPagedResultOfOSellerProductStatusHistory> readSellerProductHistoryWithHttpInfo(String xRequestedBy, String sellerProductId) throws ApiException {
        Call call = this.readSellerProductHistoryValidateBeforeCall(xRequestedBy, sellerProductId, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiPagedResultOfOSellerProductStatusHistory>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public CoupangBasicResponse changeSalePrice(String xRequestedBy, Long vendorItemId, Long price, Boolean forceSalePriceUpdate) throws ApiException {
        ApiResponse<CoupangBasicResponse> resp = this.changeSalePriceUsingPUTWithHttpInfo(xRequestedBy, vendorItemId, price, forceSalePriceUpdate);
        return (CoupangBasicResponse)resp.getData();
    }

    public ApiResponse<CoupangBasicResponse> changeSalePriceUsingPUTWithHttpInfo(String xRequestedBy, Long vendorItemId, Long price, Boolean forceSalePriceUpdate) throws ApiException {
        Call call = this.changeSalePriceUsingPUTValidateBeforeCall(xRequestedBy, vendorItemId, price, forceSalePriceUpdate, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangBasicResponse>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public CoupangBasicResponse changeOriginalPrice(String xRequestedBy, Long vendorItemId, Long originalPrice) throws ApiException {
        ApiResponse<CoupangBasicResponse> resp = this.changeOriginalPriceUsingPUTWithHttpInfo(xRequestedBy, vendorItemId, originalPrice);
        return (CoupangBasicResponse)resp.getData();
    }

    public ApiResponse<CoupangBasicResponse> changeOriginalPriceUsingPUTWithHttpInfo(String xRequestedBy, Long vendorItemId, Long originalPrice) throws ApiException {
        Call call = this.changeOriginalPriceUsingPUTValidateBeforeCall(xRequestedBy, vendorItemId, originalPrice, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangBasicResponse>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public OpenApiResult changeInventoryQuantity(String xRequestedBy, Long vendorItemId, Integer quantity) throws ApiException {
        ApiResponse<OpenApiResult> resp = this.changeInventoryQuantityWithHttpInfo(xRequestedBy, vendorItemId, quantity);
        return (OpenApiResult)resp.getData();
    }

    public ApiResponse<OpenApiResult> changeInventoryQuantityWithHttpInfo(String xRequestedBy, Long vendorItemId, Integer quantity) throws ApiException {
        Call call = this.changeInventoryQuantityValidateBeforeCall(xRequestedBy, vendorItemId, quantity, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<OpenApiResult>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    public CoupangProductListResponse searchSellerProductList(Long nextToken, Integer maxPerPage, String vendorId, String status) throws ApiException {
        ApiResponse<CoupangProductListResponse> resp = this.searchSellerProductWithHttpInfo(vendorId, nextToken, maxPerPage, null, null, null, vendorId, null, status, null, null, null, null);
        return (CoupangProductListResponse)resp.getData();
    }

    public CoupangProductListResponse searchSellerProduct(Long nextToken, Integer maxPerPage, Long sellerProductId, String sellerProductName, Long productId, String vendorId, String status, String manufacture, String brand, String createdAt, Long vendorItemId) throws ApiException {
        ApiResponse<CoupangProductListResponse> resp = this.searchSellerProductWithHttpInfo(vendorId, nextToken, maxPerPage, sellerProductId, sellerProductName, productId, vendorId, null, status, manufacture, brand, createdAt, vendorItemId);
        return (CoupangProductListResponse)resp.getData();
    }

    public ApiResponse<CoupangProductListResponse> searchSellerProductWithHttpInfo(String xRequestedBy, Long nextToken, Integer maxPerPage, Long sellerProductId, String sellerProductName, Long productId, String vendorId, String mdId, String status, String manufacture, String brand, String createdAt, Long vendorItemId) throws ApiException {
        Call call = this.searchSellerProductValidateBeforeCall(xRequestedBy, nextToken, maxPerPage, sellerProductId, sellerProductName, productId, vendorId, mdId, status, manufacture, brand, createdAt, vendorItemId, (ProgressResponseBody.ProgressListener)null, (ProgressRequestBody.ProgressRequestListener)null);
        Type localVarReturnType = (new TypeToken<CoupangProductListResponse>() {
        }).getType();
        return this.apiClient.execute(call, localVarReturnType);
    }

    private Call readSellerProductHistoryValidateBeforeCall(String xRequestedBy, String sellerProductId, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling readSellerProductHistory(Async)");
        } else if (sellerProductId == null) {
            throw new ApiException("Missing the required parameter 'sellerProductId' when calling readSellerProductHistory(Async)");
        } else {
            Call call = this.readSellerProductHistoryCall(xRequestedBy, sellerProductId, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call readSellerProductHistoryCall(String xRequestedBy, String sellerProductId, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products/{sellerProductId}/histories".replaceAll("\\{format\\}", "json").replaceAll("\\{sellerProductId\\}", this.apiClient.escapeString(sellerProductId.toString()));
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

    private Call getOutboundShippingPlaceValidateBeforeCall(String vendorId, Integer pageNum, Integer pageSize, List<Long> placeCodes, List<String> placeNames, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getOutboundShippingPlace(Async)");
        } else {
            Call call = this.getOutboundShippingPlaceCall(vendorId, pageNum, pageSize, placeCodes, placeNames, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call getOutboundShippingPlaceCall(String vendorId, Integer pageNum, Integer pageSize, List<Long> placeCodes, List<String> placeNames, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/marketplace_openapi/apis/api/v1/vendor/shipping-place/outbound".replaceAll("\\{format\\}", "json");
        List<Pair> localVarQueryParams = new ArrayList();
        if (pageNum != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "pageNum", pageNum));
        }

        if (pageSize != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "pageSize", pageSize));
        }

        if (placeCodes != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("multi", "placeCodes", placeCodes));
        }

        if (placeNames != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("multi", "placeNames", placeNames));
        }

        Map<String, String> localVarHeaderParams = new HashMap();
        if (vendorId != null) {
            localVarHeaderParams.put("vendorId", this.apiClient.parameterToString(vendorId));
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

    private Call getReturnShippingCenterValidateBeforeCall(String vendorId, String xRequestedBy, Integer pageNum, Integer pageSize, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (vendorId == null) {
            throw new ApiException("Missing the required parameter 'vendorId' when calling getReturnShippingCenter(Async)");
        } else {
            Call call = this.getReturnShippingCenterCall(vendorId, xRequestedBy, pageNum, pageSize, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call getReturnShippingCenterCall(String vendorId, String xRequestedBy, Integer pageNum, Integer pageSize, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/openapi/apis/api/v4/vendors/{vendorId}/returnShippingCenters".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorId\\}", this.apiClient.escapeString(vendorId.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
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

    private Call getCategoryRelatedMetaByDisplayCategoryCodeValidateBeforeCall(String xRequestedBy, String displayCategoryCode, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling getCategoryRelatedMetaByDisplayCategoryCode(Async)");
        } else if (displayCategoryCode == null) {
            throw new ApiException("Missing the required parameter 'displayCategoryCode' when calling getCategoryRelatedMetaByDisplayCategoryCode(Async)");
        } else {
            Call call = this.getCategoryRelatedMetaByDisplayCategoryCodeCall(xRequestedBy, displayCategoryCode, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call getCategoryRelatedMetaByDisplayCategoryCodeCall(String xRequestedBy, String displayCategoryCode, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/meta/category-related-metas/display-category-codes/{displayCategoryCode}".replaceAll("\\{format\\}", "json").replaceAll("\\{displayCategoryCode\\}", this.apiClient.escapeString(displayCategoryCode.toString()));
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

    private Call doProductCategorizationUsingPOSTValidateBeforeCall(String xRequestedBy, AutoCategorizationRequestDto autoCategorizationRequestDto, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling doProductCategorizationUsingPOST(Async)");
        } else if (autoCategorizationRequestDto == null) {
            throw new ApiException("Missing the required parameter 'autoCategorizationRequestDto' when calling doProductCategorizationUsingPOST(Async)");
        } else {
            Call call = this.doProductCategorizationUsingPOSTCall(xRequestedBy, autoCategorizationRequestDto, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call doProductCategorizationUsingPOSTCall(String xRequestedBy, AutoCategorizationRequestDto autoCategorizationRequestDto, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/openapi/apis/api/v1/categorization/predict".replaceAll("\\{format\\}", "json");
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
        this.apiClient.setApiKey(this.GenerateToken("POST", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "POST", localVarQueryParams, autoCategorizationRequestDto, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call stopSellingValidateBeforeCall(String xRequestedBy, Long vendorItemId, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling stopSelling(Async)");
        } else if (vendorItemId == null) {
            throw new ApiException("Missing the required parameter 'vendorItemId' when calling stopSelling(Async)");
        } else {
            Call call = this.stopSellingCall(xRequestedBy, vendorItemId, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call stopSellingCall(String xRequestedBy, Long vendorItemId, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/sales/stop".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorItemId\\}", this.apiClient.escapeString(vendorItemId.toString()));
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
        this.apiClient.setApiKey(this.GenerateToken("PUT", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call deleteSellerProductValidateBeforeCall(String xRequestedBy, String sellerProductId, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling deleteSellerProduct(Async)");
        } else if (sellerProductId == null) {
            throw new ApiException("Missing the required parameter 'sellerProductId' when calling deleteSellerProduct(Async)");
        } else {
            Call call = this.deleteSellerProductCall(xRequestedBy, sellerProductId, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call deleteSellerProductCall(String xRequestedBy, String sellerProductId, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
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
        this.apiClient.setApiKey(this.GenerateToken("DELETE", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
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

    private Call createSellerProductValidateBeforeCall(String xRequestedBy, OSellerProduct oSellerProduct, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling createSellerProduct(Async)");
        } else if (oSellerProduct == null) {
            throw new ApiException("Missing the required parameter 'oSellerProduct' when calling createSellerProduct(Async)");
        } else {
            Call call = this.createSellerProductCall(xRequestedBy, oSellerProduct, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call createSellerProductCall(String xRequestedBy, OSellerProduct oSellerProduct, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products".replaceAll("\\{format\\}", "json");
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
        this.apiClient.setApiKey(this.GenerateToken("POST", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "POST", localVarQueryParams, oSellerProduct, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call changeSalePriceUsingPUTValidateBeforeCall(String xRequestedBy, Long vendorItemId, Long price, Boolean forceSalePriceUpdate, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling changeSalePriceUsingPUT(Async)");
        } else if (vendorItemId == null) {
            throw new ApiException("Missing the required parameter 'vendorItemId' when calling changeSalePriceUsingPUT(Async)");
        } else if (price == null) {
            throw new ApiException("Missing the required parameter 'price' when calling changeSalePriceUsingPUT(Async)");
        } else {
            Call call = this.changeSalePriceUsingPUTCall(xRequestedBy, vendorItemId, price, forceSalePriceUpdate, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call changeSalePriceUsingPUTCall(String xRequestedBy, Long vendorItemId, Long price, Boolean forceSalePriceUpdate, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/prices/{price}".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorItemId\\}", this.apiClient.escapeString(vendorItemId.toString())).replaceAll("\\{price\\}", this.apiClient.escapeString(price.toString()));
        List<Pair> localVarQueryParams = new ArrayList();
        if (forceSalePriceUpdate != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "forceSalePriceUpdate", forceSalePriceUpdate));
        }

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
        this.apiClient.setApiKey(this.GenerateToken("PUT", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call changeOriginalPriceUsingPUTValidateBeforeCall(String xRequestedBy, Long vendorItemId, Long originalPrice, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling changeOriginalPriceUsingPUT(Async)");
        } else if (vendorItemId == null) {
            throw new ApiException("Missing the required parameter 'vendorItemId' when calling changeOriginalPriceUsingPUT(Async)");
        } else if (originalPrice == null) {
            throw new ApiException("Missing the required parameter 'originalPrice' when calling changeOriginalPriceUsingPUT(Async)");
        } else {
            Call call = this.changeOriginalPriceUsingPUTCall(xRequestedBy, vendorItemId, originalPrice, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call changeOriginalPriceUsingPUTCall(String xRequestedBy, Long vendorItemId, Long originalPrice, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/original-prices/{originalPrice}".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorItemId\\}", this.apiClient.escapeString(vendorItemId.toString())).replaceAll("\\{originalPrice\\}", this.apiClient.escapeString(originalPrice.toString()));
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
        this.apiClient.setApiKey(this.GenerateToken("PUT", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call changeInventoryQuantityValidateBeforeCall(String xRequestedBy, Long vendorItemId, Integer quantity, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling changeInventoryQuantity(Async)");
        } else if (vendorItemId == null) {
            throw new ApiException("Missing the required parameter 'vendorItemId' when calling changeInventoryQuantity(Async)");
        } else if (quantity == null) {
            throw new ApiException("Missing the required parameter 'quantity' when calling changeInventoryQuantity(Async)");
        } else {
            Call call = this.changeInventoryQuantityCall(xRequestedBy, vendorItemId, quantity, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call changeInventoryQuantityCall(String xRequestedBy, Long vendorItemId, Integer quantity, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/vendor-items/{vendorItemId}/quantities/{quantity}".replaceAll("\\{format\\}", "json").replaceAll("\\{vendorItemId\\}", this.apiClient.escapeString(vendorItemId.toString())).replaceAll("\\{quantity\\}", this.apiClient.escapeString(quantity.toString()));
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
        this.apiClient.setApiKey(this.GenerateToken("PUT", this.generatePath(localVarPath, localVarQueryParams), this.secretKey, this.accessKey));
        return this.apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    private Call searchSellerProductValidateBeforeCall(String xRequestedBy, Long nextToken, Integer maxPerPage, Long sellerProductId, String sellerProductName, Long productId, String vendorId, String mdId, String status, String manufacture, String brand, String createdAt, Long vendorItemId, ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        if (xRequestedBy == null) {
            throw new ApiException("Missing the required parameter 'xRequestedBy' when calling searchSellerProduct(Async)");
        } else {
            Call call = this.searchSellerProductCall(xRequestedBy, nextToken, maxPerPage, sellerProductId, sellerProductName, productId, vendorId, mdId, status, manufacture, brand, createdAt, vendorItemId, progressListener, progressRequestListener);
            return call;
        }
    }

    private Call searchSellerProductCall(String xRequestedBy, Long nextToken, Integer maxPerPage, Long sellerProductId, String sellerProductName, Long productId, String vendorId, String mdId, String status, String manufacture, String brand, String createdAt, Long vendorItemId, final ProgressResponseBody.ProgressListener progressListener, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        String localVarPath = "/v2/providers/seller_api/apis/api/v1/marketplace/seller-products".replaceAll("\\{format\\}", "json");
        List<Pair> localVarQueryParams = new ArrayList();
        if (nextToken != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "nextToken", nextToken));
        }

        if (maxPerPage != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "maxPerPage", maxPerPage));
        }

        if (sellerProductId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "sellerProductId", sellerProductId));
        }

        if (sellerProductName != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "sellerProductName", sellerProductName));
        }

        if (productId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "productId", productId));
        }

        if (vendorId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "vendorId", vendorId));
        }

        if (mdId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "mdId", mdId));
        }

        if (status != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "status", status));
        }

        if (manufacture != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "manufacture", manufacture));
        }

        if (brand != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "brand", brand));
        }

        if (createdAt != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "createdAt", createdAt));
        }

        if (vendorItemId != null) {
            localVarQueryParams.addAll(this.apiClient.parameterToPairs("", "vendorItemId", vendorItemId));
        }

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
