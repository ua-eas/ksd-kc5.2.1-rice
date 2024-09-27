package edu.arizona.kim.eds;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;




public class UaEdsDccRelationTest {
    private static final List<String> knownGood;
    private static final List<String> pastFailures;
    static {
        knownGood = new ArrayList<>();
        knownGood.add("Affiliate:00910:9903:Philanthropy & Alumni Engagement:I:20180730:20190208:NON");
        knownGood.add("Research Associate:00920:9011:BIO5 Institute:I:20230918:20240630:NON");
        knownGood.add("Volunteer:00979:1540:County Office - Pima County:I:20200818:20231231:VM");
        knownGood.add("Volunteer:00979:9011:BIO5 Institute:I:20190416:20190630:");

        pastFailures = new ArrayList<>();
        pastFailures.add("New Hire- Pending Approval:00995:::I:::");
        pastFailures.add("Administrative Support Assistant II:00995::Cancer Center Division:I:20240930::");
        pastFailures.add("New Hire- Pending Approval:00995:3221::I:20240916:19000101:");
    }


    @Test
    public void testKnownGood() {
        for (String relation : knownGood) {
            assertTrue(parsedSuccessfully(relation));
        }
    }


    @Test
    public void testPastFailures() {
        for (String relation : pastFailures) {
            assertTrue(parsedSuccessfully(relation));
        }
    }


    private boolean parsedSuccessfully(String relation) {
        try {
            new UaEdsDccRelation(relation);
        } catch (Exception e) {
            System.out.println("Failed to create relation from string: '" + relation + "'");
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

}
