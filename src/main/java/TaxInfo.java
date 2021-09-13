import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaxInfo {
    private double moreThan;
    private double lessThan;
    private double percent;
    private double deduction;

    @Override
    public String toString() {
        return "TaxInfo{" +
                "moreThan=" + moreThan +
                ", lessThan=" + lessThan +
                ", percent=" + percent +
                ", deduction=" + deduction +
                '}';
    }
}
