package by.intro.dms.model.feign;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @SerializedName("CharCode")
    private String charCode;
    @SerializedName("Nominal")
    private int nominal;
    @SerializedName("Name")
    private String name;
    @SerializedName("Value")
    private float value;
}
