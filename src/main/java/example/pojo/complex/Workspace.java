package example.pojo.complex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

// @JsonInclude(JsonInclude.Include.NON_DEFAULT) - can be included at the class level or variable level
// deserialization -> allowsetters
// serialization -> allowgetters

@JsonIgnoreProperties(value = "id", allowSetters = true) // will only be used for deserialization
public class Workspace {
    private String id;
    private String name;
    private String type;
    private String description;

    @JsonIgnore // ignores variable in the serialisation and deserialisation
    private int i;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // ignore maps and arrays with empty values
    private HashMap<String, String > myHashMap;

    public Workspace() {
    }

    public Workspace(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public HashMap<String, String> getMyHashMap() {
        return myHashMap;
    }

    public void setMyHashMap(HashMap<String, String> myHashMap) {
        this.myHashMap = myHashMap;
    }

}
