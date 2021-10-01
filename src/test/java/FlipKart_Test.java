import com.Common.Base;
import com.Flipkart.FlipKart_POM;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;

public class FlipKart_Test extends Base {

    FlipKart_POM flipKart;

    @Test
    @Parameters("site")
    public void getInsuranceInfo(String site) throws IOException {
        flipKart = new FlipKart_POM(driver);
        flipKart.exitLogin();
        flipKart.searchItem("Books");
        flipKart.checkRedirection();
    }
}
