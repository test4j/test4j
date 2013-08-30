package org.jtester.spec.util;

import java.util.List;

import org.jtester.spec.annotations.Given;
import org.jtester.spec.annotations.Named;
import org.jtester.spec.annotations.StoryFile;
import org.jtester.spec.annotations.StoryType;
import org.jtester.spec.inner.IScenario;
import org.jtester.testng.JSpec;
import org.testng.annotations.Test;

@StoryFile(type = StoryType.TXT)
public class ParaConverterSpec extends JSpec {

    @Test(dataProvider = "story")
    @Override
    public void runScenario(IScenario scenario) throws Throwable {
        this.run(scenario);
    }

    @Given
    public void doInit(final @Named("变量") GenericDTO dto// <br>
    ) throws Exception {
        want.string(dto.getUserName()).isEqualTo("darui.wudr");
        List<?> generic = dto.getAddresses();
        want.list(generic).sizeEq(2);
        want.object(generic.get(0)).clazIs(AddressDTO.class);
        System.out.println(dto);
    }

    public static class GenericDTO {
        String           userName;

        List<AddressDTO> addresses;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<AddressDTO> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<AddressDTO> addresses) {
            this.addresses = addresses;
        }
    }

    public static class AddressDTO {
        String address;
        String postcode;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }
    }
}
