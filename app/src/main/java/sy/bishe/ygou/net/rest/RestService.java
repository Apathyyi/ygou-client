package sy.bishe.ygou.net.rest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RestService {
    /**
     * 不传递路由信息，以键值对存储
     * @param url
     * @param params
     * @return
     */
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String,Object> params);

    /**
     *
     * @param url
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String,Object> params);

    /**
     *
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    /**
     *
     * @param url
     * @param
     * @return
     */
    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url,@FieldMap Map<String,Object> params);

    /**
     *
     * @param url
     * @param body
     * @return
     */
    @PUT
    Call<String> putRaw(@Url String url,@Body RequestBody body);

    /**
     * 、
     * @param url
     * @param params
     * @return
     */
    @DELETE
    Call<String> delete(@Url String url,@QueryMap Map<String,Object> params);

    /**
     *
     * @param url
     * @param params
     * @return
     */
    @Streaming
    @GET
    Call<ResponseBody> downLoad(@Url String url, @QueryMap Map<String,Object> params);

    /**
     *
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST
    Call<String> upLoad(@Url String url, @Part("file") MultipartBody file);

    @Multipart
    @POST
    Call<String> upLoad(@Url String url, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST
    Call<String> upLoad(@Url String url, @PartMap Map<String,RequestBody> params);
}
