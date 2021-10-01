import com.Common.Base;
import com.Turtlemint.TurtleMint_POM;
import org.testng.annotations.Test;

public class TurtleMint_Test extends Base {

    TurtleMint_POM turtleMint;

    @Test
    public void getInsuranceInfo() throws InterruptedException {
        turtleMint = new TurtleMint_POM(driver);
        turtleMint.selectInsurance();
        turtleMint.setGender("Male");
        turtleMint.setDOB("15/10/1951");
        turtleMint.setChew("No");
        turtleMint.setAnnualIncome(6);
        turtleMint.setAssuredValue("1.25 Crs");
        turtleMint.setDetails("Abhishek", "9954133645", "abhishek.bhikru@gmail.com");
        turtleMint.viewDetails("HDFC");
        System.out.println(turtleMint.getURL());
    }
}