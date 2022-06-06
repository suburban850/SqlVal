package checker;

import checker.rules.ContainsRule;
import checker.rules.OrderRule;
import checker.rules.Rule;
import lombok.Getter;
import lombok.Setter;
import resource.implementation.InformationResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
@Getter
@Setter
//
public class Checker {
    private Stack<String> stack;
    private List<Rule> rules;
    private DescriptionRepository descriptionRepository;
    public Checker()
    {
        stack = new Stack<>();
        rules = new ArrayList<>();
        descriptionRepository = new DescriptionRepository();

        rules.add(new ContainsRule(descriptionRepository));
        rules.add(new OrderRule(descriptionRepository));
    }

    public void check(String sql, InformationResource ir)
    {
        for(Rule r : rules)
        {
            String s = r.check(sql,ir);
            stack.push(s);
        }
    }
}
