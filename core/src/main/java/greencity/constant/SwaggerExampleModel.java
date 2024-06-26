package greencity.constant;

public final class SwaggerExampleModel {
    private static final String IMAGE_DESCRIPTION = "pass image as base64 or upload image\n";

    private static final String BEFORE_EXAMPLE = "<div>\n"
        + "\t<ul class=\"tab\">\n"
        + "\t\t<li class=\"tabitem active\">\n"
        + "\t\t\t<a class=\"tablinks\" data-name=\"example\">Example Value</a>\n"
        + "\t\t</li>\n"
        + "\t\t<li class=\"tabitem\">\n"
        + "\t\t\t<a class=\"tablinks\" data-name=\"model\">Model</a>\n"
        + "\t\t</li>\n"
        + "\t</ul>\n"
        + "\t<pre>\n";

    private static final String EXAMPLE =
        "  \"image\": \"string\",\n"
            + "  \"source\": \"https://example.org/\",\n"
            + "  \"shortInfo\": \"string\",\n"
            + "  \"tags\": [\n"
            + "    \"string\"\n"
            + "  ],\n"
            + "  \"titleTranslation\":\n"
            + "     {\"content\": \"string\",\n"
            + "     \"languageCode\": \"string\"},\n"
            + "  \"textTranslation\":\n"
            + "     {\"content\": \"string\",\n"
            + "     \"languageCode\": \"string\"}\n";

    private static final String EXAMPLE_FOR_ADD_ECO_NEWS =
            "  \"tags\": [\"string\"],\n"
                    + "  \"text\": \"string\",\n"
                    + "  \"title\": \"string\",\n"
                    + "  \"source\": \"string\"\n";

    private static final String AFTER_EXAMPLE = "\t</pre>\n"
        + "</div>";

    public static final String USER_PROFILE_PICTURE_DTO =
        "User Profile Picture\n"
            + BEFORE_EXAMPLE
            + "{\n"
            + "  \"id\": 0,\n"
            + "  \"profilePicturePath\": \"string\"\n"
            + "}\n"
            + AFTER_EXAMPLE;

    public static final String ADD_ECO_NEWS_REQUEST =
        "Add Eco News Request\n"
            + IMAGE_DESCRIPTION
            + BEFORE_EXAMPLE
            + "{\n"
            + EXAMPLE_FOR_ADD_ECO_NEWS
            + "}\n"
            + AFTER_EXAMPLE;
    public static final String UPDATE_ECO_NEWS =
        "Update Eco News\n"
            + IMAGE_DESCRIPTION
            + BEFORE_EXAMPLE
            + "{\n"
            + "  \"id\": 0,\n"
            + EXAMPLE
            + "}\n"
            + AFTER_EXAMPLE;
    public static final String ADD_EVENT = BEFORE_EXAMPLE
        + "{\n"
        + "\t\"title\":\"string\",\n"
        + "\t\"description\":\"string\",\n"
        + "\t\"open\":true,\n"
        + "\t\"mainImageNumber\":1,\n"
        + "\t\"daysInfo\":[\n"
        + "\t\t{\n"
        + "\t\t\t\"allDay\":false,\n"
        + "\t\t\t\"dayNumber\":1,\n"
        + "\t\t\t\"status\":\"ONLINE_OFFLINE\",\n"
        + "\t\t\t\"startDateTime\": \"2025-06-27T00:00:00Z\",\n"
        + "\t\t\t\"endDateTime\": \"2025-06-27T23:59:00Z\",\n"
        + "\t\t\t\"link\":\"https://url\",\n"
        + "\t\t\t\"address\": {\n"
        + "\t\t\t\t\"latitude\":0,\n"
        + "\t\t\t\t\"longitude\":0,\n"
        + "\t\t\t\t\"addressEn\":\"Konovaltsia street, 1, Kyiv, Ukraine\",\n"
        + "\t\t\t\t\"addressUa\":\"Коновальця вулиця, 1, Київ, Україна\"\n"
        + "\t\t\t}\n"
        + "\t\t}\n"
        + "\t],\n"
        + "\t\"tags\": [\n"
        + "\t\t\"Social\",\n"
        + "\t\t\"Environmental\",\n"
        + "\t\t\"Economic\"\n"
        + "\t]\n"
        + "}" + AFTER_EXAMPLE;

    public static final String UPDATE_EVENT = BEFORE_EXAMPLE
            + "{\n"
            + "\t\"id\": 1,\n"
            + "\t\"title\": \"Green City Conference\",\n"
            + "\t\"description\": \"Annual conference on urban sustainability and green technologies.\",\n"
            + "\t\"dateTimes\": [\n"
            + "\t\t{\n"
            + "\t\t\t\"isAllDay\": false,\n"
            + "\t\t\t\"startDateTime\": \"2023-10-15T09:00:00Z\",\n"
            + "\t\t\t\"endDateTime\": \"2023-10-15T17:00:00Z\",\n"
            + "\t\t\t\"dayNumber\": 1,\n"
            + "\t\t\t\"status\": \"ONLINE_OFFLINE\",\n"
            + "\t\t\t\"link\": \"https://event-link.com\",\n"
            + "\t\t\t\"address\": {\n"
            + "\t\t\t\t\"latitude\": 40.712776,\n"
            + "\t\t\t\t\"longitude\": -74.005974,\n"
            + "\t\t\t\t\"addressUa\": \"Вулиця Незалежності, 1\",\n"
            + "\t\t\t\t\"addressEn\": \"Independence Street, 1\"\n"
            + "\t\t\t}\n"
            + "\t\t}\n"
            + "\t],\n"
            + "\t\"tagNames\": [\"Environment\", \"Technology\"],\n"
            + "\t\"isOpen\": true\n"
            + "}";

    private SwaggerExampleModel() {
    }
}
