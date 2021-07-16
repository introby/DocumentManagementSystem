package by.intro.dms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private String id;
    private int numCode;
    private String charCode;
    private int nominal;
    private String name;
    private float value;
    private float previous;

    @JsonProperty("ID")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("NumCode")
    public void setNumCode(int numCode) {
        this.numCode = numCode;
    }

    @JsonProperty("CharCode")
    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    @JsonProperty("Nominal")
    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Value")
    public void setValue(float value) {
        this.value = value;
    }

    @JsonProperty("Previous")
    public void setPrevious(float previous) {
        this.previous = previous;
    }
}
