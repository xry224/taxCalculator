import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final AllTaxInfo taxInfo = new AllTaxInfo();
    public static void main(String[] args) throws Exception {
        configInit();
        Scanner scanner = new Scanner(System.in);
        double monthSalary;
        double yearCount;
        double extraDeduction;
        System.out.print("输入月薪：");
        monthSalary = scanner.nextDouble();

        System.out.println("=============================");
        System.out.print("输入年终奖月数：");
        yearCount = scanner.nextDouble();

        System.out.println("=============================");
        System.out.print("输入附加扣除项：");
        extraDeduction = scanner.nextDouble();

        System.out.println("=============================");
        individualTaxCal(monthSalary, extraDeduction);

        System.out.println("=============================");
        double yearTax = taxInfo.calYearTax(monthSalary, yearCount);
        System.out.format("年终上税：%f，到手：%f\n", yearTax, monthSalary * yearCount - yearTax);
    }

    public static void individualTaxCal(double monthSalary, double extraDeduction) {
        double baseTax = taxInfo.callBaseTax(monthSalary);
        System.out.println("五险一金：" + baseTax);
        baseTax = 5591;
        double taxSum = 0;
        for (int i = 1; i <= 12; ++i) {
            double deduct = (baseTax + 5000 + extraDeduction);
            double currentSum = (monthSalary - deduct) * i;

            double tax = taxInfo.calTax(currentSum) - taxSum;
            taxSum += tax;
            System.out.format("第%d月交税：%f，到手%f\n", i, tax, monthSalary - tax - baseTax);
        }
        System.out.println("月薪总共交税：" + taxSum);
    }

    public static void configInit() throws Exception {
        JsonNode node = mapper.readTree(new File("src/main/resources/config/config.json"));
        JsonNode baseTax = node.path("baseTax");
        JsonNode individualTax = node.path("individualTaxRate");
        JsonNode yearTax = node.path("yearBonusTax");

        taxInfo.setHousingFund(baseTax.get("housingFund").asDouble() / 100.0);
        taxInfo.setMedicalInsurance(baseTax.get("medicalInsurance").asDouble() / 100.0);
        taxInfo.setOldInsurance(baseTax.get("oldInsurance").asDouble() / 100.0);
        taxInfo.setJobInsurance(baseTax.get("jobInsurance").asDouble() / 100.0);

        for (int i = 1; i <= 7; ++i) {
            String rankStr = "rank" + i;
            TaxInfo individual = mapper.readValue(individualTax.path(rankStr).toString(), TaxInfo.class);
            TaxInfo year = mapper.readValue(yearTax.path(rankStr).toString(), TaxInfo.class);
            taxInfo.addIndividualTax(individual);
            taxInfo.addYearTax(year);
        }
    }

}
