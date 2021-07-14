package by.intro.dms.model.feign;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Valute {

    @SerializedName("USD")
    Currency usd;
    @SerializedName("BYN")
    Currency byn;
    @SerializedName("EUR")
    Currency eur;

}
