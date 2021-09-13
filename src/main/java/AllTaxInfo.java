import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class AllTaxInfo {
    /**
     *base tax rate
     */
    private double housingFund;
    private double oldInsurance;
    private double medicalInsurance;
    private double jobInsurance;
    /**
     * individual income tax
     * 左开右闭区间
     */
    private List<TaxInfo> individualTaxRate;
    /**
     * year-end bonus tax
     */
    private List<TaxInfo> yearBonusTax;

    public AllTaxInfo() {
        individualTaxRate = new ArrayList<>();
        yearBonusTax = new ArrayList<>();
    }

    public void addIndividualTax(TaxInfo taxInfo) {
        individualTaxRate.add(taxInfo);
    }

    public void addYearTax(TaxInfo taxInfo) {
        yearBonusTax.add(taxInfo);
    }

    public double callBaseTax(double monthSalary) {
        double res = 0;
        res += monthSalary * housingFund;
        res += monthSalary * oldInsurance;
        res += monthSalary * jobInsurance;
        res += monthSalary * medicalInsurance;
        return res;
    }

    public double calTax(double currentSum) {
        double res = 0;
        for (TaxInfo taxInfo : individualTaxRate) {
            if (taxInfo.getLessThan() >= currentSum && currentSum > taxInfo.getMoreThan()) {
                double rate = taxInfo.getPercent() / 100.0;
                res += currentSum * rate - taxInfo.getDeduction();
                break;
            }
        }
        return res;
    }

    public double calYearTax(double monthSalary, double count) {
        double res = 0;
        double sumYear = monthSalary * count;
        double rank = sumYear / 12.0;
        for (TaxInfo taxInfo : yearBonusTax) {
            if (rank <= taxInfo.getLessThan() && rank > taxInfo.getMoreThan()) {
                double rate = taxInfo.getPercent() / 100.0;
                res = sumYear * rate - taxInfo.getDeduction();
                return res;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "AllTaxInfo{" +
                "housingFund=" + housingFund +
                ", oldInsurance=" + oldInsurance +
                ", medicalInsurance=" + medicalInsurance +
                ", jobInsurance=" + jobInsurance +
                ", individualTaxRate=" + individualTaxRate +
                ", yearBonusTax=" + yearBonusTax +
                '}';
    }
}
