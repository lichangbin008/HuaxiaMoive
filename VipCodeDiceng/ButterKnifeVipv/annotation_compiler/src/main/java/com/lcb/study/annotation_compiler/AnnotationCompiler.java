package com.lcb.study.annotation_compiler;

import com.google.auto.service.AutoService;
import com.lcb.study.annotation.BindString;
import com.lcb.study.annotation.BindView;
import com.lcb.study.annotation.OnClick;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 注解处理器 目的是替我们去写findViewById的代码
 */
@AutoService(Processor.class)
public class AnnotationCompiler extends AbstractProcessor {

    //这个对象是用来创建文件的
    Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        logUtil("111111111");
    }


    /**
     * 声明注解处理器支持的java源版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 声明注解处理器要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(BindString.class.getCanonicalName());
        return types;
    }

    /**
     * 注解处理器的核心方法 所有要做的业务逻辑事情都在这个方法里面做
     *
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //将所有的节点都以类的方式一一对应起来
        Map<TypeElement, ElementForType> map = findAndParserTarget(roundEnvironment);
        if (map.size() > 0) {
            Iterator<TypeElement> iterator = map.keySet().iterator();
            Writer writer = null;
            while (iterator.hasNext()) {
                //类节点
                TypeElement typeElement = iterator.next();
                //获取到这个类节点下面的成员变量以及方法节点
                ElementForType elementForType = map.get(typeElement);
                //获取真正的类名
                String clazzName = typeElement.getSimpleName().toString();
                //获取到包名
                String packageName = getPackageName(typeElement);
                //创建一个新的类名
                String newClazzName = clazzName + "$$ViewBinder";
                //创建java文件
                try {
                    JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + newClazzName);
                    writer = sourceFile.openWriter();
                    StringBuffer stringBuffer = getStringBuffer(packageName, newClazzName, typeElement, elementForType);
                    writer.write(stringBuffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        return false;
    }

    /**
     * 获取到所有的注解 以及将注解和Activity一一对应起来
     *
     * @param roundEnvironment
     */
    private Map<TypeElement, ElementForType> findAndParserTarget(RoundEnvironment roundEnvironment) {
        Map<TypeElement, ElementForType> map = new HashMap<>();
        //获取到应用中被BindView标记了的节点
        Set<? extends Element> viewElements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //获取到所有的被OnClick标记了的方法节点
        Set<? extends Element> methodElements = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        //获取到所有的被BindString标记了的节点
        Set<? extends Element> stringElements = roundEnvironment.getElementsAnnotatedWith(BindString.class);
        //遍历所有的节点，然后让他们跟自己的类节点一一对应起来
        for (Element viewElement : viewElements) {
            //转换为成员变量节点
            VariableElement variableElement = (VariableElement) viewElement;
            //获取到这个成员变量的类节点
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //获取到TypeElement在map中的值
            ElementForType elementForType = map.get(typeElement);
            List<VariableElement> viewElements1;
            if (elementForType != null) {
                //获取到map中对应的类的封装对象中的成员变量节点的集合
                viewElements1 = elementForType.getViewElements();
                if (viewElements1 == null) {
                    viewElements1 = new ArrayList<>();
                    elementForType.setViewElements(viewElements1);
                }
            } else {
                //如果为空就创建
                elementForType = new ElementForType();
                //创建一个viewElements1集合
                viewElements1 = new ArrayList<>();
                elementForType.setViewElements(viewElements1);
                if (!map.containsKey(typeElement)) {
                    map.put(typeElement, elementForType);
                }
            }
            viewElements1.add(variableElement);

        }

        //遍历所有方法的节点，然后让他们跟自己的类节点一一对应起来
        for (Element methodElement : methodElements) {
            //转换为成员变量节点
            ExecutableElement executableElement = (ExecutableElement) methodElement;
            //获取到这个方法的类节点
            TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
            //获取到TypeElement在map中的值
            ElementForType elementForType = map.get(typeElement);
            List<ExecutableElement> methodlements1;
            if (elementForType != null) {
                //获取到map中对应的类的封装对象中的成员变量节点的集合
                methodlements1 = elementForType.getMethodElements();
                if (methodlements1 == null) {
                    methodlements1 = new ArrayList<>();
                    elementForType.setMethodElements(methodlements1);
                }
            } else {
                //如果为空就创建
                elementForType = new ElementForType();
                //创建一个methodlements1集合
                methodlements1 = new ArrayList<>();
                elementForType.setMethodElements(methodlements1);
                if (!map.containsKey(typeElement)) {
                    map.put(typeElement, elementForType);
                }
            }
            methodlements1.add(executableElement);

        }

        //遍历所有String成员变量的节点，然后让他们跟自己的类节点一一对应起来
        for (Element stringElement : stringElements) {
            //转换为成员变量节点
            VariableElement variableElement = (VariableElement) stringElement;
            //获取到这个方法的类节点
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            //获取到TypeElement在map中的值
            ElementForType elementForType = map.get(typeElement);
            List<VariableElement> stringElements1;
            if (elementForType != null) {
                //获取到map中对应的类的封装对象中的成员变量节点的集合
                stringElements1 = elementForType.getStringElements();
                if (stringElements1 == null) {
                    stringElements1 = new ArrayList<>();
                    elementForType.setStringElements(stringElements1);
                }
            } else {
                //如果为空就创建
                elementForType = new ElementForType();
                //创建一个methodlements1集合
                stringElements1 = new ArrayList<>();
                elementForType.setStringElements(stringElements1);
                if (!map.containsKey(typeElement)) {
                    map.put(typeElement, elementForType);
                }
            }
            stringElements1.add(variableElement);

        }
        return map;
    }

    /**
     * 打印日志的方法
     *
     * @param log
     */
    public void logUtil(String log) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, log);
    }

    /**
     * 获取包名的方法
     *
     * @param typeElement
     */
    public String getPackageName(Element typeElement) {
        //获取包名
        PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(typeElement);
        Name qualifiedName = packageOf.getQualifiedName();
        return qualifiedName.toString();
    }

    /**
     * 获取到类的拼装语句的方法
     *
     * @param packageName
     * @param newClazzName
     * @param typeElement
     * @param elementForType
     * @return
     */
    public StringBuffer getStringBuffer(String packageName, String newClazzName,
                                        TypeElement typeElement, ElementForType elementForType) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package " + packageName + ";\n");
        stringBuffer.append("import android.view.View;\n");
        stringBuffer.append("public class " + newClazzName + "{\n");
        stringBuffer.append("public " + newClazzName + "(final " + typeElement.getQualifiedName() + " target){\n");
        if (elementForType != null && elementForType.getViewElements() != null && elementForType.getViewElements().size() > 0) {
            List<VariableElement> viewElements = elementForType.getViewElements();
            for (VariableElement viewElement : viewElements) {
                //获取到类型
                TypeMirror typeMirror = viewElement.asType();
                //获取到控件的名字
                Name simpleName = viewElement.getSimpleName();
                //获取到资源ID
                int resId = viewElement.getAnnotation(BindView.class).value();
                stringBuffer.append("target." + simpleName + " =(" + typeMirror + ")target.findViewById(" + resId + ");\n");
            }
        }

        if (elementForType != null && elementForType.getMethodElements() != null && elementForType.getMethodElements().size() > 0) {
            List<ExecutableElement> methodElements = elementForType.getMethodElements();
            for (ExecutableElement methodElement : methodElements) {
                int[] resIds = methodElement.getAnnotation(OnClick.class).value();
                String methodName = methodElement.getSimpleName().toString();
                for (int resId : resIds) {
                    stringBuffer.append("(target.findViewById(" + resId + ")).setOnClickListener(new View.OnClickListener() {\n");
                    stringBuffer.append("public void onClick(View p0) {\n");
                    stringBuffer.append("target." + methodName + "(p0);\n");
                    stringBuffer.append("}\n});\n");
                }
            }
        }
        if (elementForType != null && elementForType.getStringElements() != null && elementForType.getStringElements().size() > 0) {
            List<VariableElement> stringElements = elementForType.getStringElements();
            for (VariableElement stringElement : stringElements) {
                //获取到类型
                TypeMirror typeMirror = stringElement.asType();
                //获取到控件的名字
                Name simpleName = stringElement.getSimpleName();
                //获取到资源ID
                int resId = stringElement.getAnnotation(BindString.class).value();
                stringBuffer.append("target." + simpleName + " =(" + typeMirror + ")target.getResources().getString(" + resId + ");\n");
            }
        }
        stringBuffer.append("}\n}\n");
        return stringBuffer;
    }
}
