// Generated: canonical android.R$color constant name -> resource id map.
// Source: javap -v -cp <sdk>/platforms/android-36/android.jar "android.R$color"
// (209 stub-field ConstantValue rows, fetched 2026-10-19) - the same names
// every ROM overlay customizes per the OEM/RRO model: only values differ
// across manufacturers, so the constant names are the stable user-facing
// spelling (README Configuration). A few named ids carry no baseline value
// in SystemColors; config overrides still work for them (the host answers
// what the config overlays, and the named gap stays for the rest).
// Regen: re-extract and rewrite this table (additive on disk).
// Guest dex never references this class - host config seam only
// (tartine.config.HostConfig).
package tartine.fw;

import java.util.HashMap;
import java.util.Map;

public final class SystemResourceNames {

    private static final Map<String, Integer> ID_BY_NAME = new HashMap<>();

    static {
        ID_BY_NAME.put("background_dark", 17170446);
        ID_BY_NAME.put("background_light", 17170447);
        ID_BY_NAME.put("black", 17170444);
        ID_BY_NAME.put("darker_gray", 17170432);
        ID_BY_NAME.put("holo_blue_bright", 17170459);
        ID_BY_NAME.put("holo_blue_dark", 17170451);
        ID_BY_NAME.put("holo_blue_light", 17170450);
        ID_BY_NAME.put("holo_green_dark", 17170453);
        ID_BY_NAME.put("holo_green_light", 17170452);
        ID_BY_NAME.put("holo_orange_dark", 17170457);
        ID_BY_NAME.put("holo_orange_light", 17170456);
        ID_BY_NAME.put("holo_purple", 17170458);
        ID_BY_NAME.put("holo_red_dark", 17170455);
        ID_BY_NAME.put("holo_red_light", 17170454);
        ID_BY_NAME.put("primary_text_dark", 17170433);
        ID_BY_NAME.put("primary_text_dark_nodisable", 17170434);
        ID_BY_NAME.put("primary_text_light", 17170435);
        ID_BY_NAME.put("primary_text_light_nodisable", 17170436);
        ID_BY_NAME.put("secondary_text_dark", 17170437);
        ID_BY_NAME.put("secondary_text_dark_nodisable", 17170438);
        ID_BY_NAME.put("secondary_text_light", 17170439);
        ID_BY_NAME.put("secondary_text_light_nodisable", 17170440);
        ID_BY_NAME.put("system_accent1_0", 17170487);
        ID_BY_NAME.put("system_accent1_10", 17170488);
        ID_BY_NAME.put("system_accent1_100", 17170490);
        ID_BY_NAME.put("system_accent1_1000", 17170499);
        ID_BY_NAME.put("system_accent1_200", 17170491);
        ID_BY_NAME.put("system_accent1_300", 17170492);
        ID_BY_NAME.put("system_accent1_400", 17170493);
        ID_BY_NAME.put("system_accent1_50", 17170489);
        ID_BY_NAME.put("system_accent1_500", 17170494);
        ID_BY_NAME.put("system_accent1_600", 17170495);
        ID_BY_NAME.put("system_accent1_700", 17170496);
        ID_BY_NAME.put("system_accent1_800", 17170497);
        ID_BY_NAME.put("system_accent1_900", 17170498);
        ID_BY_NAME.put("system_accent2_0", 17170500);
        ID_BY_NAME.put("system_accent2_10", 17170501);
        ID_BY_NAME.put("system_accent2_100", 17170503);
        ID_BY_NAME.put("system_accent2_1000", 17170512);
        ID_BY_NAME.put("system_accent2_200", 17170504);
        ID_BY_NAME.put("system_accent2_300", 17170505);
        ID_BY_NAME.put("system_accent2_400", 17170506);
        ID_BY_NAME.put("system_accent2_50", 17170502);
        ID_BY_NAME.put("system_accent2_500", 17170507);
        ID_BY_NAME.put("system_accent2_600", 17170508);
        ID_BY_NAME.put("system_accent2_700", 17170509);
        ID_BY_NAME.put("system_accent2_800", 17170510);
        ID_BY_NAME.put("system_accent2_900", 17170511);
        ID_BY_NAME.put("system_accent3_0", 17170513);
        ID_BY_NAME.put("system_accent3_10", 17170514);
        ID_BY_NAME.put("system_accent3_100", 17170516);
        ID_BY_NAME.put("system_accent3_1000", 17170525);
        ID_BY_NAME.put("system_accent3_200", 17170517);
        ID_BY_NAME.put("system_accent3_300", 17170518);
        ID_BY_NAME.put("system_accent3_400", 17170519);
        ID_BY_NAME.put("system_accent3_50", 17170515);
        ID_BY_NAME.put("system_accent3_500", 17170520);
        ID_BY_NAME.put("system_accent3_600", 17170521);
        ID_BY_NAME.put("system_accent3_700", 17170522);
        ID_BY_NAME.put("system_accent3_800", 17170523);
        ID_BY_NAME.put("system_accent3_900", 17170524);
        ID_BY_NAME.put("system_background_dark", 17170581);
        ID_BY_NAME.put("system_background_light", 17170538);
        ID_BY_NAME.put("system_control_activated_dark", 17170599);
        ID_BY_NAME.put("system_control_activated_light", 17170556);
        ID_BY_NAME.put("system_control_highlight_dark", 17170601);
        ID_BY_NAME.put("system_control_highlight_light", 17170558);
        ID_BY_NAME.put("system_control_normal_dark", 17170600);
        ID_BY_NAME.put("system_control_normal_light", 17170557);
        ID_BY_NAME.put("system_error_0", 17170629);
        ID_BY_NAME.put("system_error_10", 17170630);
        ID_BY_NAME.put("system_error_100", 17170632);
        ID_BY_NAME.put("system_error_1000", 17170641);
        ID_BY_NAME.put("system_error_200", 17170633);
        ID_BY_NAME.put("system_error_300", 17170634);
        ID_BY_NAME.put("system_error_400", 17170635);
        ID_BY_NAME.put("system_error_50", 17170631);
        ID_BY_NAME.put("system_error_500", 17170636);
        ID_BY_NAME.put("system_error_600", 17170637);
        ID_BY_NAME.put("system_error_700", 17170638);
        ID_BY_NAME.put("system_error_800", 17170639);
        ID_BY_NAME.put("system_error_900", 17170640);
        ID_BY_NAME.put("system_error_container_dark", 17170597);
        ID_BY_NAME.put("system_error_container_light", 17170554);
        ID_BY_NAME.put("system_error_dark", 17170595);
        ID_BY_NAME.put("system_error_light", 17170552);
        ID_BY_NAME.put("system_neutral1_0", 17170461);
        ID_BY_NAME.put("system_neutral1_10", 17170462);
        ID_BY_NAME.put("system_neutral1_100", 17170464);
        ID_BY_NAME.put("system_neutral1_1000", 17170473);
        ID_BY_NAME.put("system_neutral1_200", 17170465);
        ID_BY_NAME.put("system_neutral1_300", 17170466);
        ID_BY_NAME.put("system_neutral1_400", 17170467);
        ID_BY_NAME.put("system_neutral1_50", 17170463);
        ID_BY_NAME.put("system_neutral1_500", 17170468);
        ID_BY_NAME.put("system_neutral1_600", 17170469);
        ID_BY_NAME.put("system_neutral1_700", 17170470);
        ID_BY_NAME.put("system_neutral1_800", 17170471);
        ID_BY_NAME.put("system_neutral1_900", 17170472);
        ID_BY_NAME.put("system_neutral2_0", 17170474);
        ID_BY_NAME.put("system_neutral2_10", 17170475);
        ID_BY_NAME.put("system_neutral2_100", 17170477);
        ID_BY_NAME.put("system_neutral2_1000", 17170486);
        ID_BY_NAME.put("system_neutral2_200", 17170478);
        ID_BY_NAME.put("system_neutral2_300", 17170479);
        ID_BY_NAME.put("system_neutral2_400", 17170480);
        ID_BY_NAME.put("system_neutral2_50", 17170476);
        ID_BY_NAME.put("system_neutral2_500", 17170481);
        ID_BY_NAME.put("system_neutral2_600", 17170482);
        ID_BY_NAME.put("system_neutral2_700", 17170483);
        ID_BY_NAME.put("system_neutral2_800", 17170484);
        ID_BY_NAME.put("system_neutral2_900", 17170485);
        ID_BY_NAME.put("system_on_background_dark", 17170582);
        ID_BY_NAME.put("system_on_background_light", 17170539);
        ID_BY_NAME.put("system_on_error_container_dark", 17170598);
        ID_BY_NAME.put("system_on_error_container_light", 17170555);
        ID_BY_NAME.put("system_on_error_dark", 17170596);
        ID_BY_NAME.put("system_on_error_light", 17170553);
        ID_BY_NAME.put("system_on_primary_container_dark", 17170570);
        ID_BY_NAME.put("system_on_primary_container_light", 17170527);
        ID_BY_NAME.put("system_on_primary_dark", 17170572);
        ID_BY_NAME.put("system_on_primary_fixed", 17170614);
        ID_BY_NAME.put("system_on_primary_fixed_variant", 17170615);
        ID_BY_NAME.put("system_on_primary_light", 17170529);
        ID_BY_NAME.put("system_on_secondary_container_dark", 17170574);
        ID_BY_NAME.put("system_on_secondary_container_light", 17170531);
        ID_BY_NAME.put("system_on_secondary_dark", 17170576);
        ID_BY_NAME.put("system_on_secondary_fixed", 17170618);
        ID_BY_NAME.put("system_on_secondary_fixed_variant", 17170619);
        ID_BY_NAME.put("system_on_secondary_light", 17170533);
        ID_BY_NAME.put("system_on_surface_dark", 17170584);
        ID_BY_NAME.put("system_on_surface_disabled", 17170627);
        ID_BY_NAME.put("system_on_surface_light", 17170541);
        ID_BY_NAME.put("system_on_surface_variant_dark", 17170593);
        ID_BY_NAME.put("system_on_surface_variant_light", 17170550);
        ID_BY_NAME.put("system_on_tertiary_container_dark", 17170578);
        ID_BY_NAME.put("system_on_tertiary_container_light", 17170535);
        ID_BY_NAME.put("system_on_tertiary_dark", 17170580);
        ID_BY_NAME.put("system_on_tertiary_fixed", 17170622);
        ID_BY_NAME.put("system_on_tertiary_fixed_variant", 17170623);
        ID_BY_NAME.put("system_on_tertiary_light", 17170537);
        ID_BY_NAME.put("system_outline_dark", 17170594);
        ID_BY_NAME.put("system_outline_disabled", 17170628);
        ID_BY_NAME.put("system_outline_light", 17170551);
        ID_BY_NAME.put("system_outline_variant_dark", 17170625);
        ID_BY_NAME.put("system_outline_variant_light", 17170624);
        ID_BY_NAME.put("system_palette_key_color_neutral_dark", 17170610);
        ID_BY_NAME.put("system_palette_key_color_neutral_light", 17170567);
        ID_BY_NAME.put("system_palette_key_color_neutral_variant_dark", 17170611);
        ID_BY_NAME.put("system_palette_key_color_neutral_variant_light", 17170568);
        ID_BY_NAME.put("system_palette_key_color_primary_dark", 17170607);
        ID_BY_NAME.put("system_palette_key_color_primary_light", 17170564);
        ID_BY_NAME.put("system_palette_key_color_secondary_dark", 17170608);
        ID_BY_NAME.put("system_palette_key_color_secondary_light", 17170565);
        ID_BY_NAME.put("system_palette_key_color_tertiary_dark", 17170609);
        ID_BY_NAME.put("system_palette_key_color_tertiary_light", 17170566);
        ID_BY_NAME.put("system_primary_container_dark", 17170569);
        ID_BY_NAME.put("system_primary_container_light", 17170526);
        ID_BY_NAME.put("system_primary_dark", 17170571);
        ID_BY_NAME.put("system_primary_fixed", 17170612);
        ID_BY_NAME.put("system_primary_fixed_dim", 17170613);
        ID_BY_NAME.put("system_primary_light", 17170528);
        ID_BY_NAME.put("system_secondary_container_dark", 17170573);
        ID_BY_NAME.put("system_secondary_container_light", 17170530);
        ID_BY_NAME.put("system_secondary_dark", 17170575);
        ID_BY_NAME.put("system_secondary_fixed", 17170616);
        ID_BY_NAME.put("system_secondary_fixed_dim", 17170617);
        ID_BY_NAME.put("system_secondary_light", 17170532);
        ID_BY_NAME.put("system_surface_bright_dark", 17170590);
        ID_BY_NAME.put("system_surface_bright_light", 17170547);
        ID_BY_NAME.put("system_surface_container_dark", 17170587);
        ID_BY_NAME.put("system_surface_container_high_dark", 17170588);
        ID_BY_NAME.put("system_surface_container_high_light", 17170545);
        ID_BY_NAME.put("system_surface_container_highest_dark", 17170589);
        ID_BY_NAME.put("system_surface_container_highest_light", 17170546);
        ID_BY_NAME.put("system_surface_container_light", 17170544);
        ID_BY_NAME.put("system_surface_container_low_dark", 17170585);
        ID_BY_NAME.put("system_surface_container_low_light", 17170542);
        ID_BY_NAME.put("system_surface_container_lowest_dark", 17170586);
        ID_BY_NAME.put("system_surface_container_lowest_light", 17170543);
        ID_BY_NAME.put("system_surface_dark", 17170583);
        ID_BY_NAME.put("system_surface_dim_dark", 17170591);
        ID_BY_NAME.put("system_surface_dim_light", 17170548);
        ID_BY_NAME.put("system_surface_disabled", 17170626);
        ID_BY_NAME.put("system_surface_light", 17170540);
        ID_BY_NAME.put("system_surface_variant_dark", 17170592);
        ID_BY_NAME.put("system_surface_variant_light", 17170549);
        ID_BY_NAME.put("system_tertiary_container_dark", 17170577);
        ID_BY_NAME.put("system_tertiary_container_light", 17170534);
        ID_BY_NAME.put("system_tertiary_dark", 17170579);
        ID_BY_NAME.put("system_tertiary_fixed", 17170620);
        ID_BY_NAME.put("system_tertiary_fixed_dim", 17170621);
        ID_BY_NAME.put("system_tertiary_light", 17170536);
        ID_BY_NAME.put("system_text_hint_inverse_dark", 17170606);
        ID_BY_NAME.put("system_text_hint_inverse_light", 17170563);
        ID_BY_NAME.put("system_text_primary_inverse_dark", 17170602);
        ID_BY_NAME.put("system_text_primary_inverse_disable_only_dark", 17170604);
        ID_BY_NAME.put("system_text_primary_inverse_disable_only_light", 17170561);
        ID_BY_NAME.put("system_text_primary_inverse_light", 17170559);
        ID_BY_NAME.put("system_text_secondary_and_tertiary_inverse_dark", 17170603);
        ID_BY_NAME.put("system_text_secondary_and_tertiary_inverse_disabled_dark", 17170605);
        ID_BY_NAME.put("system_text_secondary_and_tertiary_inverse_disabled_light", 17170562);
        ID_BY_NAME.put("system_text_secondary_and_tertiary_inverse_light", 17170560);
        ID_BY_NAME.put("tab_indicator_text", 17170441);
        ID_BY_NAME.put("tertiary_text_dark", 17170448);
        ID_BY_NAME.put("tertiary_text_light", 17170449);
        ID_BY_NAME.put("transparent", 17170445);
        ID_BY_NAME.put("white", 17170443);
        ID_BY_NAME.put("widget_edittext_dark", 17170442);
    }

    private SystemResourceNames() {
    }

    /**
     * Resource id for a canonical android.R$color constant name (spelled
     * exactly as in the LOCAL SDK jar - lowercase, digits, underscores),
     * or {@code null} when unknown.
     */
    public static Integer idForName(String name) {
        return ID_BY_NAME.get(name);
    }
}
