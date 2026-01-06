package at.fhtw.converter;

import at.fhtw.orm.Param;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class ParamIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public PropertyName findNameForSerialization(Annotated member) {
        Param param = member.getAnnotation(Param.class);
        if (param != null) {
            return PropertyName.construct(param.name());
        }
        return super.findNameForSerialization(member);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated member) {
        Param param = member.getAnnotation(Param.class);
        if (param != null) {
            return PropertyName.construct(param.name());
        }
        return super.findNameForDeserialization(member);
    }
}
