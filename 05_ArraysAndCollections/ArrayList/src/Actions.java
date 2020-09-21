public enum Actions
{
    LIST("LIST"),
    ADD("ADD\\s+.+"),
    ADD_TO_INDEX("ADD\\s+\\d+\\s+.+"),
    EDIT("EDIT\\s+\\d+\\s+.+"),
    DELETE("DELETE\\s+\\d+"),
    EXIT("EXIT");

    private final String regex;

    public String getRegex() {
        return regex;
    }

    Actions(String regex) {
        this.regex = regex;
    }
}
