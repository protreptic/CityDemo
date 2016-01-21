package name.peterbukhal.android.citydemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class City implements Parcelable {

    private Long id;

    private String name;

    private String apiUrl;

    private String domain;

    private String mobileServer;

    private String docUrl;

    private Double latitude;

    private Double longitude;

    private Double spnLatitude;

    private Double spnLongitude;

    private Long lastAppAndroidVersion;

    private String androidDriverApkLink;

    private String[] inAppPayMethods;

    private Boolean transfers;

    private Long experimentalEconomPlus;

    private Long experimentalEconomPlusTime;

    private Boolean registrationPromocode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMobileServer() {
        return mobileServer;
    }

    public void setMobileServer(String mobileServer) {
        this.mobileServer = mobileServer;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpnLatitude() {
        return spnLatitude;
    }

    public void setSpnLatitude(Double spnLatitude) {
        this.spnLatitude = spnLatitude;
    }

    public Double getSpnLongitude() {
        return spnLongitude;
    }

    public void setSpnLongitude(Double spnLongitude) {
        this.spnLongitude = spnLongitude;
    }

    public Long getLastAppAndroidVersion() {
        return lastAppAndroidVersion;
    }

    public void setLastAppAndroidVersion(Long lastAppAndroidVersion) {
        this.lastAppAndroidVersion = lastAppAndroidVersion;
    }

    public String getAndroidDriverApkLink() {
        return androidDriverApkLink;
    }

    public void setAndroidDriverApkLink(String androidDriverApkLink) {
        this.androidDriverApkLink = androidDriverApkLink;
    }

    public String[] getInAppPayMethods() {
        return inAppPayMethods;
    }

    public void setInAppPayMethods(String[] inAppPayMethods) {
        this.inAppPayMethods = inAppPayMethods;
    }

    public Boolean getTransfers() {
        return transfers;
    }

    public void setTransfers(Boolean transfers) {
        this.transfers = transfers;
    }

    public Long getExperimentalEconomPlus() {
        return experimentalEconomPlus;
    }

    public void setExperimentalEconomPlus(Long experimentalEconomPlus) {
        this.experimentalEconomPlus = experimentalEconomPlus;
    }

    public Long getExperimentalEconomPlusTime() {
        return experimentalEconomPlusTime;
    }

    public void setExperimentalEconomPlusTime(Long experimentalEconomPlusTime) {
        this.experimentalEconomPlusTime = experimentalEconomPlusTime;
    }

    public Boolean getRegistrationPromocode() {
        return registrationPromocode;
    }

    public void setRegistrationPromocode(Boolean registrationPromocode) {
        this.registrationPromocode = registrationPromocode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(domain);
        dest.writeString(mobileServer);
        dest.writeString(docUrl);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(spnLatitude);
        dest.writeDouble(spnLongitude);
        dest.writeLong(lastAppAndroidVersion);
        dest.writeString(androidDriverApkLink);
        dest.writeString(null);
        dest.writeInt(transfers ? 1 : 0);
        dest.writeLong(experimentalEconomPlus == null ? 0 : experimentalEconomPlus);
        dest.writeLong(experimentalEconomPlusTime == null ? 0 : experimentalEconomPlusTime);
        dest.writeInt(registrationPromocode ? 1 : 0);
    }

    public City() {}

    private City(Parcel parcel) {
        id = parcel.readLong();
        name = parcel.readString();
        domain = parcel.readString();
        mobileServer = parcel.readString();
        docUrl = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        spnLatitude = parcel.readDouble();
        spnLongitude = parcel.readDouble();
        lastAppAndroidVersion = parcel.readLong();
        androidDriverApkLink = parcel.readString();
        //
        transfers = parcel.readInt() != 0;
        experimentalEconomPlus = parcel.readLong();
        experimentalEconomPlusTime = parcel.readLong();
        registrationPromocode = parcel.readInt() != 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {

        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        public City[] newArray(int size) {
            return new City[size];
        }

    };

    public static class CityJsonSerializer implements JsonSerializer<City> {

        @Override
        public JsonElement serialize(City src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("city_id", src.getId());
            jsonObject.addProperty("city_name", src.getName());
            jsonObject.addProperty("city_api_url", src.getApiUrl());

            return jsonObject;
        }

    }

    public static class CityJsonDeserializer implements JsonDeserializer<City> {

        @Override
        public City deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            City city = new City();
            city.setId(jsonObject.get("city_id").getAsLong());
            city.setName(jsonObject.get("city_name").getAsString());
            city.setApiUrl(jsonObject.get("city_api_url").getAsString());
            city.setDomain(jsonObject.get("city_domain").getAsString());
            city.setMobileServer(jsonObject.get("city_mobile_server").getAsString());
            city.setDocUrl(jsonObject.get("city_doc_url").getAsString());
            city.setLatitude(jsonObject.get("city_latitude").getAsDouble());
            city.setLongitude(jsonObject.get("city_longitude").getAsDouble());
            city.setSpnLatitude(jsonObject.get("city_spn_latitude").getAsDouble());
            city.setSpnLongitude(jsonObject.get("city_spn_longitude").getAsDouble());
            city.setLastAppAndroidVersion(jsonObject.get("last_app_android_version").getAsLong());
            city.setAndroidDriverApkLink(jsonObject.get("android_driver_apk_link").getAsString());

            if (jsonObject.get("inapp_pay_methods") != null) {
                JsonArray jsonElements = jsonObject.get("inapp_pay_methods").getAsJsonArray();
                int length = jsonElements.size();
                if (length > 0) {
                    String [] methods = new String [length];
                    for (int i = 0; i < length; i++) {
                        methods[i] = jsonElements.get(i).getAsString();
                    }
                    city.setInAppPayMethods(methods);
                }
            }

            city.setTransfers(jsonObject.get("transfers").getAsBoolean());
            if (jsonObject.get("experimental_econom_plus") != null)
                city.setExperimentalEconomPlus(jsonObject.get("experimental_econom_plus").getAsLong());
            if (jsonObject.get("experimental_econom_plus_time") != null)
                city.setExperimentalEconomPlusTime(jsonObject.get("experimental_econom_plus_time").getAsLong());
            if (jsonObject.get("registration_promocode") != null)
                city.setRegistrationPromocode(jsonObject.get("registration_promocode").getAsBoolean());

            return city;
        }

    }

    @Override
    public String toString() {
        return "City: \n" +
                "\tid = " + id +
                "\tname = " + name;
    }

}
