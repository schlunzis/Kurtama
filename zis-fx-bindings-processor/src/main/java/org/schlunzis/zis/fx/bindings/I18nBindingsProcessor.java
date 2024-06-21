package org.schlunzis.zis.fx.bindings;

import org.schlunzis.zis.fx.bindings.internal.I18nField;
import org.schlunzis.zis.fx.bindings.internal.I18nLabel;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@SupportedAnnotationTypes("org.schlunzis.zis.fx.bindings.I18nBinding")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class I18nBindingsProcessor extends AbstractProcessor {

    private static final String BINDINGS_FACTORY_NAME = "bf";
    private static final String CONTROLLER_NAME = "c";

    private final HashMap<String, Collection<I18nField>> classFieldMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(I18nBinding.class)) {
            if (annotatedElement.getKind() != ElementKind.FIELD) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Only fields can be annotated with " + I18nBinding.class.getSimpleName());
                return true;
            }

            final String qualifiedClassName = getClassNameForElement(annotatedElement);
            final I18nBinding annotation = annotatedElement.getAnnotation(I18nBinding.class);
            final String key = annotation.value();
            final String name = annotatedElement.getSimpleName().toString();

            classFieldMap.computeIfAbsent(qualifiedClassName, k -> new ArrayList<>()).add(new I18nLabel(name, key, CONTROLLER_NAME, BINDINGS_FACTORY_NAME));
        }

        generateAndWriteSources();
        classFieldMap.clear();
        return true;
    }

    private String getClassNameForElement(Element element) {
        return ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
    }

    private void generateAndWriteSources() {
        for (Map.Entry<String, Collection<I18nField>> entry : classFieldMap.entrySet()) {
            final String qualifiedClassName = entry.getKey();
            final Collection<I18nField> fields = entry.getValue();
            final String source = generateSource(qualifiedClassName, fields);

            Filer filer = processingEnv.getFiler();
            try {
                JavaFileObject fileObject = filer.createSourceFile(qualifiedClassName + "I18n");
                try (Writer writer = fileObject.openWriter()) {
                    writer.append(source);
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Could not write source file: " + e.getMessage());
            }
        }
    }

    private String generateSource(String qualifiedClassName, Collection<I18nField> fields) {
        final String className = qualifiedClassName.substring(qualifiedClassName.lastIndexOf('.') + 1);
        final String packageName = qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf('.'));

        final StringBuilder source = new StringBuilder();
        source.append("package ").append(packageName).append(";\n\n");
        source.append("import org.schlunzis.zis.fx.bindings.BindingsFactory;\n\n");
        source.append("public class ").append(className).append("I18n").append(" {\n\n");
        source.append("    public static void i18n(BindingsFactory ").append(BINDINGS_FACTORY_NAME).append(", ").append(className).append(" ").append(CONTROLLER_NAME).append(") {\n");
        fields.forEach(field -> source.append("        ").append(field.createBinding()).append("\n"));
        source.append("    }\n\n");
        source.append("}\n");

        final String sourceString = source.toString();
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated source:\n" + sourceString);
        return sourceString;
    }

}
