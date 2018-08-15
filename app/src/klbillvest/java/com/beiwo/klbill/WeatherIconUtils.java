package com.beiwo.klbill;

import com.beihui.market.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 * @author:
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/6
 */

public class WeatherIconUtils {
    private WeatherIconUtils() {
    }

    /**
     * 获取天气图标
     * @param type
     * @return
     */
    public static int getWeatherIcon(int type) {
        // 如果是晚上
        if (isNight(System.currentTimeMillis()))
            switch (type) {
                case Constants.SUNNY:
                    return 1;//R.drawable.ic_nightsunny_big;
                case Constants.CLOUDY:
                    return 1;//R.drawable.ic_nightcloudy_big;
                case Constants.HEAVY_RAIN:
                case Constants.LIGHT_RAIN:
                case Constants.MODERATE_RAIN:
                case Constants.SHOWER:
                case Constants.STORM:
                    return 1;//R.drawable.ic_nightrain_big;
                case Constants.SNOWSTORM:
                case Constants.LIGHT_SNOW:
                case Constants.MODERATE_SNOW:
                case Constants.HEAVY_SNOW:
                case Constants.SNOW_SHOWER:
                    return 1;//R.drawable.ic_nightsnow_big;
                default:
                    break;
            }
        // 如果是白天
        switch (type) {
            case Constants.SUNNY:
                return 1;//R.drawable.ic_sunny_big;
            case Constants.CLOUDY:
                return 1;//R.drawable.ic_cloudy_big;
            case Constants.OVERCAST:
                return 1;//R.drawable.ic_overcast_big;
            case Constants.FOGGY:
                return 1;//R.drawable.tornado_day_night;
            case Constants.SEVERE_STORM:
                return 1;//R.drawable.hurricane_day_night;
            case Constants.HEAVY_STORM:
                return 1;//R.drawable.ic_heavyrain_big;
            case Constants.STORM:
                return 1;//R.drawable.ic_heavyrain_big;
            case Constants.THUNDERSHOWER:
                return 1;//R.drawable.ic_thundeshower_big;
            case Constants.SHOWER:
                return 1;//R.drawable.ic_shower_big;
            case Constants.HEAVY_RAIN:
                return 1;//R.drawable.ic_heavyrain_big;
            case Constants.MODERATE_RAIN:
                return 1;//R.drawable.ic_moderraterain_big;
            case Constants.LIGHT_RAIN:
                return 1;//R.drawable.ic_lightrain_big;
            case Constants.SLEET:
                return 1;//R.drawable.ic_sleet_big;
            case Constants.SNOWSTORM:
                return 1;//R.drawable.ic_snow_big;
            case Constants.SNOW_SHOWER:
                return 1;//R.drawable.ic_snow_big;
            case Constants.HEAVY_SNOW:
                return 1;//R.drawable.ic_heavysnow_big;
            case Constants.MODERATE_SNOW:
                return 1;//R.drawable.ic_snow_big;
            case Constants.LIGHT_SNOW:
                return 1;//R.drawable.ic_snow_big;
            case Constants.STRONGSANDSTORM:
                return 1;//R.drawable.ic_sandstorm_big;
            case Constants.SANDSTORM:
                return 1;//R.drawable.ic_sandstorm_big;
            case Constants.SAND:
                return 1;//R.drawable.ic_sandstorm_big;
            case Constants.BLOWING_SAND:
                return 1;//R.drawable.ic_sandstorm_big;
            case Constants.ICE_RAIN:
                return 1;//R.drawable.freezing_rain_day_night;
            case Constants.DUST:
                return 1;//R.drawable.ic_dust_big;
            case Constants.HAZE:
                return 1;//R.drawable.ic_haze_big;
            default:
                return 1;//R.drawable.ic_default_big;
        }
    }

    /**
     * 获取天气清晰背景
     * @param type
     * @return
     */
    public static int getWeatherNromalBg(int type) {
        if (isNight(System.currentTimeMillis()))
            switch (type) {
                case Constants.SUNNY:
                    return 1;//R.drawable.bg_fine_night;
                case Constants.CLOUDY:
                    return 1;//R.drawable.bg_cloudy_night;
                case Constants.HEAVY_RAIN:
                case Constants.LIGHT_RAIN:
                case Constants.MODERATE_RAIN:
                case Constants.SHOWER:
                case Constants.ICE_RAIN:
                case Constants.STORM:
                    return 1;//R.drawable.bg_rain;
                case Constants.SNOWSTORM:
                case Constants.LIGHT_SNOW:
                case Constants.MODERATE_SNOW:
                case Constants.HEAVY_SNOW:
                case Constants.SNOW_SHOWER:
                    return 1;//R.drawable.bg_snow_night;
                default:
                    break;
            }
        // 如果是白天
        switch (type) {
            case Constants.SUNNY:
                return 1;//R.drawable.bg_fine_day;
            case Constants.CLOUDY:
                return 1;//R.drawable.bg_cloudy_day;
            case Constants.OVERCAST:
                return 1;//R.drawable.bg_overcast;
            case Constants.FOGGY:
                return 1;//R.drawable.bg_fog;
            case Constants.SEVERE_STORM:
            case Constants.HEAVY_STORM:
            case Constants.STORM:
                return 1;//R.drawable.bg_rain;
            case Constants.THUNDERSHOWER:
                return 1;//R.drawable.bg_thunder_storm;
            case Constants.SHOWER:
            case Constants.HEAVY_RAIN:
            case Constants.MODERATE_RAIN:
            case Constants.LIGHT_RAIN:
            case Constants.SLEET:
                return 1;//R.drawable.bg_rain;
            case Constants.SNOWSTORM:
            case Constants.SNOW_SHOWER:
            case Constants.HEAVY_SNOW:
            case Constants.MODERATE_SNOW:
            case Constants.LIGHT_SNOW:
                return 1;//R.drawable.bg_snow;
            case Constants.STRONGSANDSTORM:
            case Constants.SANDSTORM:
            case Constants.SAND:
            case Constants.BLOWING_SAND:
                return 1;//R.drawable.bg_sand_storm;
            case Constants.ICE_RAIN:
                return 1;//R.drawable.bg_rain;
            case Constants.DUST:
            case Constants.HAZE:
                return 1;//R.drawable.bg_haze;

            default:
                return 1;// R.drawable.bg_na;
        }
    }
    /**
     * 获取天气模糊背景
     * @param type
     * @return
     */
    public static int getWeatherBlurBg(int type) {
        if (isNight(System.currentTimeMillis()))
            switch (type) {
                case Constants.SUNNY:
                    return 1;//R.drawable.bg_fine_night_blur;
                case Constants.CLOUDY:
                    return 1;//R.drawable.bg_cloudy_night_blur;
                case Constants.HEAVY_RAIN:
                case Constants.LIGHT_RAIN:
                case Constants.MODERATE_RAIN:
                case Constants.SHOWER:
                case Constants.ICE_RAIN:
                    return 1;//R.drawable.bg_rain_blur;
                case Constants.STORM:
                    return 1;//R.drawable.bg_rain_blur;
                case Constants.SNOWSTORM:
                case Constants.LIGHT_SNOW:
                case Constants.MODERATE_SNOW:
                case Constants.HEAVY_SNOW:
                case Constants.SNOW_SHOWER:
                    return 1;//R.drawable.bg_snow_night_blur;
                default:
                    break;
            }
        // 如果是白天
        switch (type) {
            case Constants.SUNNY:
                return 1;//R.drawable.bg_fine_day_blur;
            case Constants.CLOUDY:
                return 1;//R.drawable.bg_cloudy_day_blur;
            case Constants.OVERCAST:
                return 1;//R.drawable.bg_overcast_blur;
            case Constants.FOGGY:
                return 1;//R.drawable.bg_fog_blur;
            case Constants.SEVERE_STORM:
            case Constants.HEAVY_STORM:
            case Constants.STORM:
                return 1;//R.drawable.bg_rain_blur;
            case Constants.THUNDERSHOWER:
                return 1;//R.drawable.bg_thunder_storm_blur;
            case Constants.SHOWER:
            case Constants.HEAVY_RAIN:
            case Constants.MODERATE_RAIN:
            case Constants.LIGHT_RAIN:
            case Constants.SLEET:
                return 1;//R.drawable.bg_rain_blur;
            case Constants.SNOWSTORM:
            case Constants.SNOW_SHOWER:
            case Constants.HEAVY_SNOW:
            case Constants.MODERATE_SNOW:
            case Constants.LIGHT_SNOW:
                return 1;//R.drawable.bg_snow_blur;
            case Constants.STRONGSANDSTORM:
            case Constants.SANDSTORM:
            case Constants.SAND:
            case Constants.BLOWING_SAND:
                return 1;//R.drawable.bg_sand_storm_blur;
            case Constants.ICE_RAIN:
                return 1;//R.drawable.bg_rain_blur;
            case Constants.DUST:
            case Constants.HAZE:
                return 1;//R.drawable.bg_haze_blur;
            default:
                return 1;//R.drawable.bg_na_blur;
        }
    }

    public static boolean isNight(long time) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String timeStr = df.format(new Date(System.currentTimeMillis()));
        // L.i("liweiping", "timeStr = " + timeStr);
        try {
            int timeHour = Integer.parseInt(timeStr);
            return (timeHour >= 18 || timeHour <= 6);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取天气清晰背景
     * @param type
     * @return
     */
    public static int getRawNromalBg(int type) {
        if (isNight(System.currentTimeMillis()))
            switch (type) {
                case Constants.SUNNY:
                    return 1;//R.drawable.bg_fine_night;
                case Constants.CLOUDY:
                    return 1;//R.drawable.bg_cloudy_night;
//			case Constants.FOGGY:
//				return R.raw.foggy_n;
//			case Constants.HEAVY_RAIN:
//			case Constants.LIGHT_RAIN:
//			case Constants.MODERATE_RAIN:
//			case Constants.SHOWER:
//			case Constants.ICE_RAIN:
//				return R.raw.rain_n;
//			case Constants.STORM:
//				return R.raw.storm_n;
                case Constants.SNOWSTORM:
                case Constants.LIGHT_SNOW:
                case Constants.MODERATE_SNOW:
                case Constants.HEAVY_SNOW:
                case Constants.SNOW_SHOWER:
                    return 1;//R.drawable.bg_snow_night;
                default:
                    break;
            }
        // 如果是白天
        switch (type) {
            case Constants.SUNNY:
                return 1;//R.drawable.bg_fine_day;
            case Constants.CLOUDY:
                return 1;//R.drawable.bg_cloudy_day;
            case Constants.OVERCAST:
                return 1;//R.drawable.bg_overcast;
            case Constants.FOGGY:
                return 1;//R.drawable.bg_fog;
            case Constants.SEVERE_STORM:
            case Constants.HEAVY_STORM:
//		case Constants.STORM:
//			return R.raw.storm_d;
            case Constants.THUNDERSHOWER:
                return 1;//R.drawable.bg_thunder_storm;
            case Constants.STORM:
            case Constants.SHOWER:
            case Constants.HEAVY_RAIN:
            case Constants.MODERATE_RAIN:
            case Constants.LIGHT_RAIN:
            case Constants.SLEET:
                return 1;//R.drawable.bg_rain;
            case Constants.SNOWSTORM:
            case Constants.SNOW_SHOWER:
            case Constants.HEAVY_SNOW:
            case Constants.MODERATE_SNOW:
            case Constants.LIGHT_SNOW:
                return 1;//R.drawable.bg_snow;
            case Constants.STRONGSANDSTORM:
            case Constants.SANDSTORM:
            case Constants.SAND:
            case Constants.BLOWING_SAND:
                return 1;//R.drawable.bg_sand_storm;
            case Constants.ICE_RAIN:
                return 1;//R.drawable.bg_rain;
            case Constants.DUST:
            case Constants.HAZE:
                return 1;//R.drawable.bg_haze;

            default:
                return 1;//R.drawable.bg_na;
        }
    }
    /**
     * 获取天气模糊背景
     * @param type
     * @return
     */
    public static int getRawBlurBg(int type) {
        if (isNight(System.currentTimeMillis()))
            switch (type) {
                case Constants.SUNNY:
                    return 1;//R.drawable.bg_fine_night_blur;
                case Constants.CLOUDY:
                    return 1;//R.drawable.bg_cloudy_night_blur;
//			case Constants.FOGGY:
//				return R.raw.foggy_n_blur;
//			case Constants.HEAVY_RAIN:
//			case Constants.LIGHT_RAIN:
//			case Constants.MODERATE_RAIN:
//			case Constants.SHOWER:
//			case Constants.ICE_RAIN:
//				return R.raw.rain_n_blur;
//			case Constants.STORM:
//				return R.raw.storm_n_blur;
                case Constants.SNOWSTORM:
                case Constants.LIGHT_SNOW:
                case Constants.MODERATE_SNOW:
                case Constants.HEAVY_SNOW:
                case Constants.SNOW_SHOWER:
                    return 1;//R.drawable.bg_snow_night_blur;
                default:
                    break;
            }
        // 如果是白天
        switch (type) {
            case Constants.SUNNY:
                return 1;//R.drawable.bg_fine_day_blur;
            case Constants.CLOUDY:
                return 1;//R.drawable.bg_cloudy_day_blur;
            case Constants.OVERCAST:
                return 1;//R.drawable.bg_overcast_blur;
            case Constants.FOGGY:
                return 1;//R.drawable.bg_fog_blur;
            case Constants.SEVERE_STORM:
            case Constants.HEAVY_STORM:
//		case Constants.STORM:
//			return R.raw.storm_d_blur;
            case Constants.THUNDERSHOWER:
                return 1;//R.drawable.bg_thunder_storm_blur;
            case Constants.STORM:
            case Constants.SHOWER:
            case Constants.HEAVY_RAIN:
            case Constants.MODERATE_RAIN:
            case Constants.LIGHT_RAIN:
            case Constants.SLEET:
                return 1;//R.drawable.bg_rain_blur;
            case Constants.SNOWSTORM:
            case Constants.SNOW_SHOWER:
            case Constants.HEAVY_SNOW:
            case Constants.MODERATE_SNOW:
            case Constants.LIGHT_SNOW:
                return 1;//R.drawable.bg_snow_blur;
            case Constants.STRONGSANDSTORM:
            case Constants.SANDSTORM:
            case Constants.SAND:
            case Constants.BLOWING_SAND:
                return 1;//R.drawable.bg_sand_storm_blur;
            case Constants.ICE_RAIN:
                return 1;//R.drawable.bg_rain_blur;
            case Constants.DUST:
            case Constants.HAZE:
                return 1;//R.drawable.bg_haze_blur;
            default:
                return 1;//R.drawable.bg_na_blur;
        }
    }

}
