package org.intellij.sonar.analysis;

import java.util.Set;

import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.GlobalSimpleInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptionsProcessor;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.intellij.sonar.index.IssuesByFileIndex;
import org.intellij.sonar.index.SonarIssue;
import org.intellij.sonar.util.Finders;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public abstract class BaseGlobalInspectionTool extends GlobalSimpleInspectionTool {

  @Override
  public void checkFile(
    @NotNull final PsiFile psiFile,
    @NotNull final InspectionManager manager,
    @NotNull final ProblemsHolder problemsHolder,
    @NotNull final GlobalInspectionContext globalContext,
    @NotNull final ProblemDescriptionsProcessor problemDescriptionsProcessor
  ) {
    Set<SonarIssue> issues = IssuesByFileIndex.getIssuesForFile(psiFile);
    for (final SonarIssue issue : issues) {
      if (!processIssue(issue)) continue;
      final ProblemHighlightType severity = SonarToIjSeverityMapping.toProblemHighlightType(issue.getSeverity());
      final TextRange textRange = Finders.getLineRange(psiFile,issue.getLine());
      final ProblemDescriptor problemDescriptor = problemsHolder.getManager().createProblemDescriptor(
        psiFile,
        textRange,
        issue.formattedMessage()+" "+issue.getRuleKey(),
        severity,
        false
      );
      problemDescriptionsProcessor.addProblemElement(
        globalContext.getRefManager().getReference(psiFile),
        problemDescriptor
      );
    }
  }

  /**
   @see com.intellij.codeInspection.InspectionEP#displayName
   @see com.intellij.codeInspection.InspectionEP#key
   @see com.intellij.codeInspection.InspectionEP#bundle
   */
  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "SonarQube Issue";
  }

  public abstract Boolean processIssue(SonarIssue issue);
}
