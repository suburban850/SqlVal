package checker.rules;

import checker.DBpojo;
import checker.DescriptionRepository;
import resource.implementation.InformationResource;

public class WhereRule implements Rule{
    DescriptionRepository descriptionRepository;

    public WhereRule(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }

    //select * from hr.employees where(max(country_id) > 100)
    @Override
    public String check(String s, InformationResource ir) {
        DBpojo dBpojo = descriptionRepository.getPOJOSSS().get(3);
        s.trim();
        String[] sk = s.split(" ");
        int whereIndex = 0;
        for(int i = 0; i<sk.length;i++)
        {
            System.out.println(sk[i] + "      sk");
            if(sk[i].equals("where"))
            {

                whereIndex=i;
                break;
            }
        }

        if(whereIndex==0)
        {
            return "true";
        }

        if(sk[whereIndex+1].substring(0,2).equals("max") || sk[whereIndex+1].substring(0,2).equals("min") ||sk[whereIndex+1].substring(0,2).equals("avg") ||sk[whereIndex+1].substring(0,4).equals("count")
      ||  sk[whereIndex+1].substring(0,2).equals("sum"))
        {
            dBpojo.setSug("AND OR: column name: contains aggregation " + sk[whereIndex+1]);
            return dBpojo.toString();
        }



        return "true";
    }

}
