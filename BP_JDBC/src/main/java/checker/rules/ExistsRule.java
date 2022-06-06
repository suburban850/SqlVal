package checker.rules;

import checker.DBpojo;
import checker.DescriptionRepository;
import resource.implementation.InformationResource;

public class ExistsRule implements Rule{
    DescriptionRepository descriptionRepository;

    public ExistsRule(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }
    //ako postoji selekt mora da postoji i from
    //ako postoji agregacija mora da postoji i group by
    //ako postoji where mora da postoji like ili in ili =
    @Override
    public String check(String s, InformationResource ir) {
        DBpojo pojo = descriptionRepository.getPOJOSSS().get(4);
        String[] sk = s.split(" ");
        if(!sk[0].equals("select")){
            pojo.setType("SELECT_MISSING");
            pojo.setDesc("Select expression missing.");
            pojo.setSug("You should add a select expression.");
            return pojo.toString();
        }
        int indexFrom=0;
        for(int i = 1; i<sk.length;i++)
        {
          if(sk[i].equals("from"))
          {
              indexFrom=i;
              break;
          }
        }
        if(indexFrom == 0)
        {
                pojo.setType("FROM_MISSING");
                pojo.setDesc("From expression missing.");
                pojo.setSug("You should add from expression");
                return pojo.toString();
        }
        int whereIndex = 0;
        for(int i = indexFrom; i<sk.length;i++)
        {
            if(sk[i].equals("where"))
            {
                whereIndex = i;
                break;
            }
        }
        if(whereIndex == 0) return "true";

        int signIndex= 0;
        for(int i = whereIndex; i<sk.length;i++)
        {
            if(sk[i].equals("=") || sk[i].equals("like") || sk[i].equals("in"))
            {
                signIndex =i;
                break;
            }
        }
        if(signIndex==0)
        {
            pojo.setType("NO_SIGN_AFTER_WHERE");
            pojo.setDesc("There is no sign after where expression");
            pojo.setSug("You should add LIKE, IN or '=' after where");
            return pojo.toString();
        }
        return "true";
    }
}
