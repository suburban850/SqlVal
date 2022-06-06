package checker.rules;

import resource.implementation.InformationResource;
//
public interface Rule {
    void check(String s, InformationResource ir);
}
