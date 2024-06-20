package org.schlunzis.zis.fx.bindings;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("org.schlunzis.zis.fx.bindings.I18nBinding")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class I18nBindingsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.getElementsAnnotatedWith(I18nBinding.class).isEmpty())
            return true;

        Filer filer = processingEnv.getFiler();
        try {
            JavaFileObject fileObject = filer.createSourceFile("org.schlunzis.kurtama.client.fx.controller.LoginControllerI18n");
            try (Writer writer = fileObject.openWriter()) {
                writer.append("""
                        package org.schlunzis.kurtama.client.fx.controller;
                                               \s
                        import org.schlunzis.kurtama.client.util.I18n;
                                               \s
                        public class LoginControllerI18n {
                                               \s
                           public static void i18n(I18n i18n, LoginController lc) {
                               lc.emailLabel.textProperty().bind(i18n.createBinding("login.label.email"));
                           }
                                               \s
                        }
                        \s""");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not write source file: " + e.getMessage());
        }
        return true;
    }

}
