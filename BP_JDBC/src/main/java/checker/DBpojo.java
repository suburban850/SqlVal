package checker;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DBpojo {
    private String type;
    private String desc;
    private String sug;

    @Override
    public String toString() {
        return  type + "\n  "+ desc +"\n"+ sug + "\n";
    }
}
