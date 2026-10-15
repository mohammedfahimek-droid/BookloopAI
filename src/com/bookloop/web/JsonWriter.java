package com.bookloop.web;

import com.bookloop.ai.ScoredBookMatch;
import com.bookloop.ai.SearchQueryIntent;
import com.bookloop.model.Book;
import com.bookloop.model.Student;
import com.bookloop.model.Transaction;
import com.bookloop.model.User;
import com.bookloop.service.BarterMatch;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lightweight, zero-dependency JSON serializer for BookLoop AI domain objects.
 */
public class JsonWriter {

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String toJson(Book b) {
        if (b == null) return "null";
        return "{" +
                "\"id\":\"" + escape(b.getId()) + "\"," +
                "\"title\":\"" + escape(b.getTitle()) + "\"," +
                "\"author\":\"" + escape(b.getAuthor()) + "\"," +
                "\"edition\":\"" + escape(b.getEdition()) + "\"," +
                "\"subject\":\"" + escape(b.getSubject()) + "\"," +
                "\"condition\":\"" + b.getCondition().name() + "\"," +
                "\"conditionLabel\":\"" + escape(b.getCondition().getLabel()) + "\"," +
                "\"mode\":\"" + b.getMode().name() + "\"," +
                "\"modeLabel\":\"" + escape(b.getMode().getDisplayName()) + "\"," +
                "\"price\":" + b.getPrice() + "," +
                "\"originalRetailPrice\":" + b.getOriginalRetailPrice() + "," +
                "\"ownerId\":\"" + (b.getOwner() != null ? escape(b.getOwner().getId()) : "") + "\"," +
                "\"ownerName\":\"" + (b.getOwner() != null ? escape(b.getOwner().getName()) : "Campus Pool") + "\"," +
                "\"neededSubject\":\"" + escape(b.getNeededSubject()) + "\"," +
                "\"neededBookTitle\":\"" + escape(b.getNeededBookTitle()) + "\"," +
                "\"status\":\"" + b.getStatus().name() + "\"" +
                "}";
    }

    public static String toJson(User u) {
        if (u == null) return "null";
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"").append(escape(u.getId())).append("\",");
        sb.append("\"name\":\"").append(escape(u.getName())).append("\",");
        sb.append("\"email\":\"").append(escape(u.getEmail())).append("\",");
        sb.append("\"campusId\":\"").append(escape(u.getCampusId())).append("\",");
        sb.append("\"department\":\"").append(escape(u.getDepartment())).append("\",");
        sb.append("\"role\":\"").append(escape(u.getRoleName())).append("\",");
        sb.append("\"karmaPoints\":").append(u.getKarmaPoints()).append(",");
        sb.append("\"totalSavings\":").append(u.getTotalSavings()).append(",");
        sb.append("\"booksShared\":").append(u.getBooksSharedCount()).append(",");
        sb.append("\"trustScore\":").append(String.format(java.util.Locale.US, "%.1f", u.calculateTrustScore())).append(",");

        if (u instanceof Student s) {
            sb.append("\"semester\":").append(s.getSemester()).append(",");
            sb.append("\"gradYear\":").append(s.getGradYear()).append(",");
            sb.append("\"wishlist\":[").append(s.getWishlist().stream()
                    .map(w -> "\"" + escape(w) + "\"")
                    .collect(Collectors.joining(","))).append("]");
        } else {
            sb.append("\"wishlist\":[]");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String toJson(ScoredBookMatch m) {
        return "{" +
                "\"score\":" + String.format(java.util.Locale.US, "%.1f", m.getMatchScore()) + "," +
                "\"explanation\":\"" + escape(m.getMatchExplanation()) + "\"," +
                "\"book\":" + toJson(m.getBook()) +
                "}";
    }

    public static String toJson(BarterMatch bm) {
        return "{" +
                "\"bookA\":" + toJson(bm.getBookA()) + "," +
                "\"bookB\":" + toJson(bm.getBookB()) + "," +
                "\"studentA\":" + toJson(bm.getStudentA()) + "," +
                "\"studentB\":" + toJson(bm.getStudentB()) + "," +
                "\"matchScore\":" + String.format(java.util.Locale.US, "%.1f", bm.getMatchScore()) + "," +
                "\"reasonA\":\"" + escape(bm.getReasonA()) + "\"," +
                "\"reasonB\":\"" + escape(bm.getReasonB()) + "\"," +
                "\"alert\":\"" + escape(bm.getFormattedAlert()) + "\"" +
                "}";
    }

    public static String toJson(SearchQueryIntent intent) {
        if (intent == null) return "{}";
        return "{" +
                "\"rawQuery\":\"" + escape(intent.getRawQuery()) + "\"," +
                "\"keywords\":[" + intent.getKeywords().stream().map(k -> "\"" + escape(k) + "\"").collect(Collectors.joining(",")) + "]," +
                "\"targetSubject\":\"" + escape(intent.getTargetSubject()) + "\"," +
                "\"maxBudget\":" + (intent.getMaxPriceConstraint() != null ? intent.getMaxPriceConstraint() : "null") + "," +
                "\"modeFilter\":\"" + (intent.getPreferredMode() != null ? intent.getPreferredMode().name() : "ALL") + "\"," +
                "\"conditionFilter\":\"" + (intent.getMinCondition() != null ? intent.getMinCondition().name() : "ANY") + "\"," +
                "\"isUrgent\":" + intent.isUrgent() +
                "}";
    }

    public static String toJson(Transaction t) {
        return "{" +
                "\"id\":\"" + escape(t.getTransactionId()) + "\"," +
                "\"bookTitle\":\"" + escape(t.getPrimaryBookTitle()) + "\"," +
                "\"senderName\":\"" + escape(t.getInitiatorName()) + "\"," +
                "\"receiverName\":\"" + escape(t.getProviderName()) + "\"," +
                "\"mode\":\"" + (t.getMode() != null ? t.getMode().name() : "") + "\"," +
                "\"cashAmount\":" + t.getAmountPaid() + "," +
                "\"karmaEarned\":" + t.getKarmaEarned() + "," +
                "\"timestamp\":\"" + escape(t.getTimestamp().toString()) + "\"," +
                "\"receipt\":\"" + escape(t.generateReceipt()) + "\"" +
                "}";
    }

    public static <T> String listToJson(List<T> list, java.util.function.Function<T, String> mapper) {
        if (list == null) return "[]";
        return "[" + list.stream().map(mapper).collect(Collectors.joining(",")) + "]";
    }
}
