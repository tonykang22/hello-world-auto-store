package com.github.kingwaggs.ordermanager.coupangsdk.service;

import com.github.kingwaggs.ordermanager.coupangsdk.auth.ApiKeyAuth;
import com.github.kingwaggs.ordermanager.coupangsdk.auth.Authentication;
import com.github.kingwaggs.ordermanager.coupangsdk.auth.HttpBasicAuth;
import com.github.kingwaggs.ordermanager.coupangsdk.auth.OAuth;
import com.github.kingwaggs.ordermanager.coupangsdk.domain.*;
import com.github.kingwaggs.ordermanager.coupangsdk.exception.ApiException;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okio.BufferedSink;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoupangMarketPlaceApiClient {
    public static final double JAVA_VERSION = Double.parseDouble(System.getProperty("java.specification.version"));
    public static final boolean IS_ANDROID;
    public static final int ANDROID_SDK_VERSION;
    public static String SDK_VERSION = "1.0.17";
    public static final String SDK_VERSION_HEADER_NAME = "X-SDK-VERSION";
    public static final String LENIENT_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private String basePath = "https://api-gateway.coupang.com";
    private boolean lenientOnJson = false;
    private boolean debugging = false;
    private Map<String, String> defaultHeaderMap = new HashMap();
    private String tempFolderPath = null;
    private Map<String, Authentication> authentications;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat datetimeFormat;
    private boolean lenientDatetimeFormat;
    private int dateLength;
    private InputStream sslCaCert;
    private boolean verifyingSsl = true;
    private OkHttpClient httpClient = new OkHttpClient();
    private JSON json = new JSON(this);

    public CoupangMarketPlaceApiClient() {
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.initDatetimeFormat();
        this.lenientDatetimeFormat = true;
        this.setUserAgent("Swagger-Codegen/1.0.0/java");
        this.authentications = new HashMap();
        this.authentications.put("api_key", new ApiKeyAuth("header", "Authorization"));
        this.authentications = Collections.unmodifiableMap(this.authentications);
    }

    public String getBasePath() {
        return this.basePath;
    }

    public CoupangMarketPlaceApiClient setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }

    public CoupangMarketPlaceApiClient setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public JSON getJSON() {
        return this.json;
    }

    public CoupangMarketPlaceApiClient setJSON(JSON json) {
        this.json = json;
        return this;
    }

    public boolean isVerifyingSsl() {
        return this.verifyingSsl;
    }

//    public CoupangMarketPlaceApiClient setVerifyingSsl(boolean verifyingSsl) {
//        this.verifyingSsl = verifyingSsl;
//        this.applySslSettings();
//        return this;
//    }

    public InputStream getSslCaCert() {
        return this.sslCaCert;
    }

//    public CoupangMarketPlaceApiClient setSslCaCert(InputStream sslCaCert) {
//        this.sslCaCert = sslCaCert;
//        this.applySslSettings();
//        return this;
//    }

    public DateFormat getDateFormat() {
        return this.dateFormat;
    }

    public CoupangMarketPlaceApiClient setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        this.dateLength = this.dateFormat.format(new Date()).length();
        return this;
    }

    public DateFormat getDatetimeFormat() {
        return this.datetimeFormat;
    }

    public CoupangMarketPlaceApiClient setDatetimeFormat(DateFormat datetimeFormat) {
        this.datetimeFormat = datetimeFormat;
        return this;
    }

    public boolean isLenientDatetimeFormat() {
        return this.lenientDatetimeFormat;
    }

    public CoupangMarketPlaceApiClient setLenientDatetimeFormat(boolean lenientDatetimeFormat) {
        this.lenientDatetimeFormat = lenientDatetimeFormat;
        return this;
    }

    public Date parseDate(String str) {
        if (str == null) {
            return null;
        } else {
            try {
                return this.dateFormat.parse(str);
            } catch (ParseException var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public Date parseDatetime(String str) {
        if (str == null) {
            return null;
        } else {
            Object format;
            if (this.lenientDatetimeFormat) {
                str = str.replaceAll("[zZ]\\z", "+0000");
                str = str.replaceAll("([+-]\\d{2}):(\\d{2})\\z", "$1$2");
                str = str.replaceAll("([+-]\\d{2})\\z", "$100");
                str = str.replaceAll("(:\\d{1,2})([+-]\\d{4})\\z", "$1.000$2");
                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            } else {
                format = this.datetimeFormat;
            }

            try {
                return ((DateFormat) format).parse(str);
            } catch (ParseException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public Date parseDateOrDatetime(String str) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= this.dateLength ? this.parseDate(str) : this.parseDatetime(str);
        }
    }

    public String formatDate(Date date) {
        return this.dateFormat.format(date);
    }

    public String formatDatetime(Date date) {
        return this.datetimeFormat.format(date);
    }

    public Map<String, Authentication> getAuthentications() {
        return this.authentications;
    }

    public Authentication getAuthentication(String authName) {
        return (Authentication) this.authentications.get(authName);
    }

    public void setUsername(String username) {
        Iterator var2 = this.authentications.values().iterator();

        Authentication auth;
        do {
            if (!var2.hasNext()) {
                throw new RuntimeException("No HTTP basic authentication configured!");
            }

            auth = (Authentication) var2.next();
        } while (!(auth instanceof HttpBasicAuth));

        ((HttpBasicAuth) auth).setUsername(username);
    }

    public void setPassword(String password) {
        Iterator var2 = this.authentications.values().iterator();

        Authentication auth;
        do {
            if (!var2.hasNext()) {
                throw new RuntimeException("No HTTP basic authentication configured!");
            }

            auth = (Authentication) var2.next();
        } while (!(auth instanceof HttpBasicAuth));

        ((HttpBasicAuth) auth).setPassword(password);
    }

    public void setApiKey(String apiKey) {
        Iterator var2 = this.authentications.values().iterator();

        Authentication auth;
        do {
            if (!var2.hasNext()) {
                throw new RuntimeException("No API key authentication configured!");
            }

            auth = (Authentication) var2.next();
        } while (!(auth instanceof ApiKeyAuth));

        ((ApiKeyAuth) auth).setApiKey(apiKey);
    }

    public void setApiKeyPrefix(String apiKeyPrefix) {
        Iterator var2 = this.authentications.values().iterator();

        Authentication auth;
        do {
            if (!var2.hasNext()) {
                throw new RuntimeException("No API key authentication configured!");
            }

            auth = (Authentication) var2.next();
        } while (!(auth instanceof ApiKeyAuth));

        ((ApiKeyAuth) auth).setApiKeyPrefix(apiKeyPrefix);
    }

    public void setAccessToken(String accessToken) {
        Iterator var2 = this.authentications.values().iterator();

        Authentication auth;
        do {
            if (!var2.hasNext()) {
                throw new RuntimeException("No OAuth2 authentication configured!");
            }

            auth = (Authentication) var2.next();
        } while (!(auth instanceof OAuth));

        ((OAuth) auth).setAccessToken(accessToken);
    }

    public CoupangMarketPlaceApiClient setUserAgent(String userAgent) {
        this.addDefaultHeader("User-Agent", userAgent);
        return this;
    }

    public CoupangMarketPlaceApiClient addDefaultHeader(String key, String value) {
        this.defaultHeaderMap.put(key, value);
        return this;
    }

    public boolean isLenientOnJson() {
        return this.lenientOnJson;
    }

    public CoupangMarketPlaceApiClient setLenientOnJson(boolean lenient) {
        this.lenientOnJson = lenient;
        return this;
    }

    public boolean isDebugging() {
        return this.debugging;
    }

//    public CoupangMarketPlaceApiClient setDebugging(boolean debugging) {
//        if (debugging != this.debugging) {
//            if (debugging) {
//                this.loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                    public void log(String s) {
//                        System.out.println(s);
//                    }
//                });
//                this.loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                this.httpClient.interceptors().add(this.loggingInterceptor);
//            } else {
//                this.httpClient.interceptors().remove(this.loggingInterceptor);
//                this.loggingInterceptor = null;
//            }
//        }
//
//        this.debugging = debugging;
//        return this;
//    }

    public String getTempFolderPath() {
        return this.tempFolderPath;
    }

    public CoupangMarketPlaceApiClient setTempFolderPath(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
        return this;
    }

    public int getConnectTimeout() {
        return this.httpClient.connectTimeoutMillis();
    }

    public CoupangMarketPlaceApiClient setConnectTimeout(int connectionTimeout) {
        this.httpClient.newBuilder().
                connectTimeout((long) connectionTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Date) {
            return this.formatDatetime((Date) param);
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();

            Object o;
            for (Iterator var3 = ((Collection) param).iterator(); var3.hasNext(); b.append(String.valueOf(o))) {
                o = var3.next();
                if (b.length() > 0) {
                    b.append(",");
                }
            }

            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    public List<Pair> parameterToPairs(String collectionFormat, String name, Object value) {
        List<Pair> params = new ArrayList();
        if (name != null && !name.isEmpty() && value != null) {
            Collection valueCollection = null;
            if (value instanceof Collection) {
                valueCollection = (Collection) value;
                if (valueCollection.isEmpty()) {
                    return params;
                } else {
                    collectionFormat = collectionFormat != null && !collectionFormat.isEmpty() ? collectionFormat : "csv";
                    if (collectionFormat.equals("multi")) {
                        Iterator var11 = valueCollection.iterator();

                        while (var11.hasNext()) {
                            Object item = var11.next();
                            params.add(new Pair(name, this.parameterToString(item)));
                        }

                        return params;
                    } else {
                        String delimiter = ",";
                        if (collectionFormat.equals("csv")) {
                            delimiter = ",";
                        } else if (collectionFormat.equals("ssv")) {
                            delimiter = " ";
                        } else if (collectionFormat.equals("tsv")) {
                            delimiter = "\t";
                        } else if (collectionFormat.equals("pipes")) {
                            delimiter = "|";
                        }

                        StringBuilder sb = new StringBuilder();
                        Iterator var8 = valueCollection.iterator();

                        while (var8.hasNext()) {
                            Object item = var8.next();
                            sb.append(delimiter);
                            sb.append(this.parameterToString(item));
                        }

                        params.add(new Pair(name, sb.substring(1)));
                        return params;
                    }
                }
            } else {
                params.add(new Pair(name, this.parameterToString(value)));
                return params;
            }
        } else {
            return params;
        }
    }

    public String sanitizeFilename(String filename) {
        return filename.replaceAll(".*[/\\\\]", "");
    }

    public boolean isJsonMime(String mime) {
        return mime != null && mime.matches("(?i)application\\/json(;.*)?");
    }

    public String selectHeaderAccept(String[] accepts) {
        if (accepts.length == 0) {
            return null;
        } else {
            String[] var2 = accepts;
            int var3 = accepts.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String accept = var2[var4];
                if (this.isJsonMime(accept)) {
                    return accept;
                }
            }

            return StringUtil.join(accepts, ",");
        }
    }

    public String selectHeaderContentType(String[] contentTypes) {
        if (contentTypes.length == 0) {
            return "application/json";
        } else {
            String[] var2 = contentTypes;
            int var3 = contentTypes.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String contentType = var2[var4];
                if (this.isJsonMime(contentType)) {
                    return contentType;
                }
            }

            return contentTypes[0];
        }
    }

    public String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException var3) {
            return str;
        }
    }

    public <T> T deserialize(Response response, Type returnType) throws ApiException {
        if (response != null && returnType != null) {
            if ("byte[]".equals(returnType.toString())) {
                try {
                    return (T) response.body().bytes();
                } catch (IOException var5) {
                    throw new ApiException(var5);
                }
            } else if (returnType.equals(File.class)) {
                return (T) this.downloadFileFromResponse(response);
            } else {
                String respBody;
                try {
                    if (response.body() != null) {
                        respBody = response.body().string();
                    } else {
                        respBody = null;
                    }
                } catch (IOException var6) {
                    throw new ApiException(var6);
                }

                if (respBody != null && !"".equals(respBody)) {
                    String contentType = response.headers().get("Content-Type");
                    if (contentType == null) {
                        contentType = "application/json";
                    }

                    if (this.isJsonMime(contentType)) {
                        return this.json.deserialize(respBody, returnType);
                    } else if (returnType.equals(String.class)) {
                        return (T) respBody;
                    } else {
                        throw new ApiException("Content type \"" + contentType + "\" is not supported for type: " + returnType, response.code(), response.headers().toMultimap(), respBody);
                    }
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public RequestBody serialize(Object obj, String contentType) throws ApiException {
        if (obj instanceof byte[]) {
            return RequestBody.create(MediaType.parse(contentType), (byte[]) ((byte[]) obj));
        } else if (obj instanceof File) {
            return RequestBody.create(MediaType.parse(contentType), (File) obj);
        } else if (this.isJsonMime(contentType)) {
            String content;
            if (obj != null) {
                content = this.json.serialize(obj);
            } else {
                content = null;
            }

            return RequestBody.create(MediaType.parse(contentType), content);
        } else {
            throw new ApiException("Content type \"" + contentType + "\" is not supported");
        }
    }

    public File downloadFileFromResponse(Response response) throws ApiException {
        try {
            File file = this.prepareDownloadFile(response);
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(response.body().source());
            sink.close();
            return file;
        } catch (IOException var4) {
            throw new ApiException(var4);
        }
    }

    public File prepareDownloadFile(Response response) throws IOException {
        String filename = null;
        String contentDisposition = response.header("Content-Disposition");
        Pattern prefixPattern;
        if (contentDisposition != null && !"".equals(contentDisposition)) {
            prefixPattern = Pattern.compile("filename=['\"]?([^'\"\\s]+)['\"]?");
            Matcher matcher = prefixPattern.matcher(contentDisposition);
            if (matcher.find()) {
                filename = this.sanitizeFilename(matcher.group(1));
            }
        }

        String suffix = null;
        String prefix;
        if (filename == null) {
            prefix = "download-";
            suffix = "";
        } else {
            int pos = filename.lastIndexOf(".");
            if (pos == -1) {
                prefix = filename + "-";
            } else {
                prefix = filename.substring(0, pos) + "-";
                suffix = filename.substring(pos);
            }

            if (prefix.length() < 3) {
                prefix = "download-";
            }
        }

        return this.tempFolderPath == null ? File.createTempFile(prefix, suffix) : File.createTempFile(prefix, suffix, new File(this.tempFolderPath));
    }

    public <T> ApiResponse<T> execute(Call call) throws ApiException {
        return this.execute(call, (Type) null);
    }

    public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
        try {
            Response response = call.execute();
            T data = this.handleResponse(response, returnType);
            return new ApiResponse(response.code(), response.headers().toMultimap(), data);
        } catch (IOException var5) {
            throw new ApiException(var5);
        }
    }

    public <T> void executeAsync(Call call, ApiCallback<T> callback) {
        this.executeAsync(call, (Type) null, callback);
    }

    public <T> void executeAsync(Call call, final Type returnType, final ApiCallback<T> callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(new ApiException(e), 0, (Map) null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Object result;
                try {
                    result = CoupangMarketPlaceApiClient.this.handleResponse(response, returnType);
                } catch (ApiException var4) {
                    callback.onFailure(var4, response.code(), response.headers().toMultimap());
                    return;
                }
                callback.onSuccess(result, response.code(), response.headers().toMultimap());
            }

        });
    }

    public <T> T handleResponse(Response response, Type returnType) throws ApiException {
        if (response.isSuccessful()) {
            return returnType != null && response.code() != 204 ? this.deserialize(response, returnType) : null;
        } else {
            String respBody = null;
            if (response.body() != null) {
                try {
                    respBody = response.body().string();
                } catch (IOException var5) {
                    throw new ApiException(response.message(), var5, response.code(), response.headers().toMultimap());
                }
            }

            throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBody);
        }
    }

    public Call buildCall(String path, String method, List<Pair> queryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams, String[] authNames, ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        this.updateParamsForAuth(authNames, queryParams, headerParams);
        String url = this.buildUrl(path, queryParams);
        Request.Builder reqBuilder = (new Request.Builder()).url(url);
        this.processHeaderParams(headerParams, reqBuilder);
        String contentType = (String) headerParams.get("Content-Type");
        if (contentType == null) {
            contentType = "application/json";
        }

        RequestBody reqBody;
        if (!HttpMethod.permitsRequestBody(method)) {
            reqBody = null;
        } else if ("application/x-www-form-urlencoded".equals(contentType)) {
            reqBody = this.buildRequestBodyFormEncoding(formParams);
        } else if ("multipart/form-data".equals(contentType)) {
            reqBody = this.buildRequestBodyMultipart(formParams);
        } else if (body == null) {
            if ("DELETE".equals(method)) {
                reqBody = null;
            } else {
                reqBody = RequestBody.create(MediaType.parse(contentType), "");
            }
        } else {
            reqBody = this.serialize(body, contentType);
        }

        Request request = null;
        if (progressRequestListener != null && reqBody != null) {
            ProgressRequestBody progressRequestBody = new ProgressRequestBody(reqBody, progressRequestListener);
            request = reqBuilder.method(method, progressRequestBody).build();
        } else {
            request = reqBuilder.method(method, reqBody).build();
        }

        return this.httpClient.newCall(request);
    }

    public String buildUrl(String path, List<Pair> queryParams) {
        StringBuilder url = new StringBuilder();
        url.append(this.basePath).append(path);
        if (queryParams != null && !queryParams.isEmpty()) {
            String prefix = path.contains("?") ? "&" : "?";
            Iterator var5 = queryParams.iterator();

            while (var5.hasNext()) {
                Pair param = (Pair) var5.next();
                if (param.getValue() != null) {
                    if (prefix != null) {
                        url.append(prefix);
                        prefix = null;
                    } else {
                        url.append("&");
                    }

                    String value = this.parameterToString(param.getValue());
                    url.append(this.escapeString(param.getName())).append("=").append(this.escapeString(value));
                }
            }
        }

        return url.toString();
    }

    public void processHeaderParams(Map<String, String> headerParams, Request.Builder reqBuilder) {
        Iterator var3 = headerParams.entrySet().iterator();

        Map.Entry header;
        while (var3.hasNext()) {
            header = (Map.Entry) var3.next();
            reqBuilder.header((String) header.getKey(), this.parameterToString(header.getValue()));
        }

        var3 = this.defaultHeaderMap.entrySet().iterator();

        while (var3.hasNext()) {
            header = (Map.Entry) var3.next();
            if (!headerParams.containsKey(header.getKey())) {
                reqBuilder.header((String) header.getKey(), this.parameterToString(header.getValue()));
            }
        }

        reqBuilder.header("X-SDK-VERSION", SDK_VERSION);
    }

    public void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams) {
        String[] var4 = authNames;
        int var5 = authNames.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String authName = var4[var6];
            Authentication auth = (Authentication) this.authentications.get(authName);
            if (auth == null) {
                throw new RuntimeException("Authentication undefined: " + authName);
            }

            auth.applyToParams(queryParams, headerParams);
        }

    }

    public RequestBody buildRequestBodyFormEncoding(Map<String, Object> formParams) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        Iterator var3 = formParams.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry) var3.next();
            formBuilder.add((String) param.getKey(), this.parameterToString(param.getValue()));
        }

        return formBuilder.build();
    }

    public RequestBody buildRequestBodyMultipart(Map<String, Object> formParams) {
        MultipartBody.Builder mpBuilder = (new MultipartBody.Builder()).setType(MultipartBody.FORM);
        Iterator var3 = formParams.entrySet().iterator();

        while (var3.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry) var3.next();
            if (param.getValue() instanceof File) {
                File file = (File) param.getValue();
                Headers partHeaders = Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + (String) param.getKey() + "\"; filename=\"" + file.getName() + "\""});
                MediaType mediaType = MediaType.parse(this.guessContentTypeFromFile(file));
                mpBuilder.addPart(partHeaders, RequestBody.create(mediaType, file));
            } else {
                Headers partHeaders = Headers.of(new String[]{"Content-Disposition", "form-data; name=\"" + (String) param.getKey() + "\""});
                mpBuilder.addPart(partHeaders, RequestBody.create((MediaType) null, this.parameterToString(param.getValue())));
            }
        }

        return mpBuilder.build();
    }

    public String guessContentTypeFromFile(File file) {
        String contentType = URLConnection.guessContentTypeFromName(file.getName());
        return contentType == null ? "application/octet-stream" : contentType;
    }

    private void initDatetimeFormat() {
        String formatWithTimeZone = null;
        if (IS_ANDROID) {
            if (ANDROID_SDK_VERSION >= 18) {
                formatWithTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
            }
        } else if (JAVA_VERSION >= 1.7D) {
            formatWithTimeZone = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        }

        if (formatWithTimeZone != null) {
            this.datetimeFormat = new SimpleDateFormat(formatWithTimeZone);
        } else {
            this.datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            this.datetimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

    }

//    private void applySslSettings() {
//        try {
//            KeyManager[] keyManagers = null;
//            TrustManager[] trustManagers = null;
//            HostnameVerifier hostnameVerifier = null;
//            X509TrustManager password;
//            if (!this.verifyingSsl) {
//                password = new X509TrustManager() {
//                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    }
//
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//                };
//                SSLContext sslContext = SSLContext.getInstance("TLS");
//                trustManagers = new TrustManager[]{password};
//                hostnameVerifier = new HostnameVerifier() {
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                };
//            } else if (this.sslCaCert != null) {
//                password = null;
//                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//                Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(this.sslCaCert);
//                if (certificates.isEmpty()) {
//                    throw new IllegalArgumentException("expected non-empty set of trusted certificates");
//                }
//
//                KeyStore caKeyStore = this.newEmptyKeyStore((char[]) null);
//                int index = 0;
//                Iterator var9 = certificates.iterator();
//
//                while (var9.hasNext()) {
//                    Certificate certificate = (Certificate) var9.next();
//                    String certificateAlias = "ca" + Integer.toString(index++);
//                    caKeyStore.setCertificateEntry(certificateAlias, certificate);
//                }
//
//                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                trustManagerFactory.init(caKeyStore);
//                trustManagers = trustManagerFactory.getTrustManagers();
//            }
//
//            if (keyManagers == null && trustManagers == null) {
//                this.httpClient.sslSocketFactory().createSocket();
//            } else {
//                SSLContext sslContext = SSLContext.getInstance("TLS");
//                sslContext.init((KeyManager[]) keyManagers, trustManagers, new SecureRandom());
//                this.httpClient.sslSocketFactory().
////                this.httpClient.setSslSocketFactory(sslContext.getSocketFactory());
//            }
//
//            this.httpClient.setHostnameVerifier(hostnameVerifier);
//        } catch (GeneralSecurityException | IOException var12) {
//            throw new RuntimeException(var12);
//        }
//    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load((InputStream) null, password);
            return keyStore;
        } catch (IOException var3) {
            throw new AssertionError(var3);
        }
    }

    static {
        boolean isAndroid;
        try {
            Class.forName("android.app.Activity");
            isAndroid = true;
        } catch (ClassNotFoundException var6) {
            isAndroid = false;
        }

        IS_ANDROID = isAndroid;
        int sdkVersion = 0;
        if (IS_ANDROID) {
            try {
                sdkVersion = Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt((Object) null);
            } catch (Exception var5) {
                try {
                    sdkVersion = Integer.parseInt((String) Class.forName("android.os.Build$VERSION").getField("SDK").get((Object) null));
                } catch (Exception var4) {
                }
            }
        }

        ANDROID_SDK_VERSION = sdkVersion;
    }

}

