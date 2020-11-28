package org.intellij.sonar.configuration.project;

import static org.intellij.sonar.util.UIUtil.makeObj;

import java.util.Collection;

import javax.swing.*;

import com.intellij.openapi.project.Project;
import org.intellij.sonar.configuration.partials.LocalAnalysisScriptView;
import org.intellij.sonar.persistence.LocalAnalysisScript;
import org.intellij.sonar.persistence.LocalAnalysisScripts;

public class ProjectLocalAnalysisScriptView extends LocalAnalysisScriptView {

  public ProjectLocalAnalysisScriptView(
    JComboBox localAnalysisScriptComboBox,
    JButton addLocalAnalysisScriptButton,
    JButton editLocalAnalysisScriptButton,
    JButton removeLocalAnalysisScriptButton,
    Project project
  ) {
    super(
      localAnalysisScriptComboBox,
      addLocalAnalysisScriptButton,
      editLocalAnalysisScriptButton,
      removeLocalAnalysisScriptButton,
      project
    );
  }

  protected void initComboBox() {
    final Collection<LocalAnalysisScript> allScripts = LocalAnalysisScripts.getAll();
    myLocalAnalysisScriptComboBox.removeAllItems();
    myLocalAnalysisScriptComboBox.addItem(makeObj(LocalAnalysisScripts.NO_LOCAL_ANALYSIS));
    for (LocalAnalysisScript script : allScripts) {
      myLocalAnalysisScriptComboBox.addItem(makeObj(script.getName()));
    }
  }

  protected boolean editAndRemoveButtonsCanBeEnabled() {
    return !LocalAnalysisScripts.NO_LOCAL_ANALYSIS.equals(myLocalAnalysisScriptComboBox.getSelectedItem().toString());
  }
}
