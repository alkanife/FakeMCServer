package me.michidik.fakemcserver;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class StringUtils {

    public static String parse(final String msg) {
        Pattern colorPattern = Pattern.compile("&([0-9a-fk-or])");

        if (msg == null)
            return null;

        if (msg.isEmpty())
            return "";

        return colorPattern.matcher(msg).replaceAll("\u00a7$1");
    }

    public static String combineList(List<String> list) {
        if (list == null)
            return "";

        if (list.size() == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();

        for (String s : list)
            stringBuilder.append(s).append("\\n");

        return stringBuilder.toString();
    }

    public static String buildServerPingResponse() {
        JSONConfiguration conf = FakeMCServer.getConfig();
        StringBuilder sb = new StringBuilder();

        // Version name
        String versionName = "Minecraft";
        if (conf.getVersion_name() != null)
            if (!conf.getVersion_name().isEmpty())
                versionName = StringUtils.parse(conf.getVersion_name());
        sb.append("{\"version\":{\"name\":\"").append(versionName).append("\",");

        // Protocol
        sb.append("\"protocol\":").append(conf.getProtocol()).append("},");

        // Max players
        sb.append("\"players\":{\"max\":").append(conf.getMax_players()).append(",");

        // Online players
        sb.append("\"online\":").append(conf.getConnected_players()).append(",");

        // Player sample
        sb.append("\"sample\":[");
        if (conf.getPlayers() != null) {
            if (conf.getPlayers().size() > 0) {
                int count = 0;
                for (final String player : FakeMCServer.getConfig().getPlayers()) {
                    if (count != 0)
                        sb.append(",");
                    count++;
                    sb.append("{\"name\":\"").append(StringUtils.parse(player)).append("\",\"id\":\"").append(UUID.randomUUID().toString()).append("\"}");
                    if (count == 10 && conf.getPlayers().size() > 10)
                        break;
                }
            }
        }
        sb.append("]},");

        // MOTD
        sb.append("\"description\":{\"text\":\"").append(conf.getMotd() == null ? "A Minecraft Server" : parse(combineList(conf.getMotd()))).append("\"},");

        // Icon
        sb.append("\"favicon\": \"").append(conf.getBase64_icon() == null ? "" : conf.getBase64_icon()).append("\" }");

        return sb.toString();
    }
}