package es.us.lsi.acme.market.services;

public class Forecast {
    private Summary Summary;
    private Summary2 Summaries[];


    public Forecast(Forecast.Summary summary, Summary2[] summaries) {
        Summary = summary;
        Summaries = summaries;
    }

    public Forecast() {
    }

    public Forecast.Summary getSummary() {
        return Summary;
    }

    public void setSummary(Forecast.Summary summary) {
        Summary = summary;
    }

    public Summary2[] getSummaries() {
        return Summaries;
    }

    public void setSummaries(Summary2[] summaries) {
        Summaries = summaries;
    }

    public class Summary {
        private String Phrase;
        private int TypeId;

        public Summary() {
        }

        public Summary(String phrase, int typeId) {
            Phrase = phrase;
            TypeId = typeId;
        }

        public String getPhrase() {
            return Phrase;
        }

        public void setPhrase(String phrase) {
            Phrase = phrase;
        }

        public int getTypeId() {
            return TypeId;
        }

        public void setTypeId(int typeId) {
            TypeId = typeId;
        }
    }

    public class Summary2 {
        private int StartMinute;
        private int EndMinute;
        private int CountMinute;
        private String MinuteText;

        public Summary2(int startMinute, int endMinute, int countMinute, String minuteText) {
            StartMinute = startMinute;
            EndMinute = endMinute;
            CountMinute = countMinute;
            MinuteText = minuteText;
        }

        public Summary2() {
        }

        public int getStartMinute() {
            return StartMinute;
        }

        public void setStartMinute(int startMinute) {
            StartMinute = startMinute;
        }

        public int getEndMinute() {
            return EndMinute;
        }

        public void setEndMinute(int endMinute) {
            EndMinute = endMinute;
        }

        public int getCountMinute() {
            return CountMinute;
        }

        public void setCountMinute(int countMinute) {
            CountMinute = countMinute;
        }

        public String getMinuteText() {
            return MinuteText;
        }

        public void setMinuteText(String minuteText) {
            MinuteText = minuteText;
        }
    }

}
