package org.eclipsecon.editor;

import java.util.Arrays;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class KeywordsPresentationReconciler extends PresentationReconciler implements IPresentationReconciler {

	private static final IToken KEYWORD_TOKEN_ATTRIBUTE = new Token(new TextAttribute(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE)));
	private static final IToken COMMENT_TOKEN_ATTRIBUTE = new Token(new TextAttribute(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY)));
	
	public KeywordsPresentationReconciler() {
		RuleBasedScanner scanner= new RuleBasedScanner();
		WordRule keywordRule = new WordRule(new JavaKeywordDetector());
		addKeywords(keywordRule);
		scanner.setRules(new IRule[] {
			keywordRule,
			new MultiLineRule("/*", "*/", COMMENT_TOKEN_ATTRIBUTE),
			new SingleLineRule("//", "", COMMENT_TOKEN_ATTRIBUTE),
		});
		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(scanner);
		this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
	}

	private void addKeywords(WordRule keywordRule) {
		Arrays.asList(new String[] {
			"package",
			"class",
			"interface",
			"implements",
			"extends",
			"if",
			"for",
			"while",
			// ,,,
		}).forEach(keyword -> keywordRule.addWord(keyword, KEYWORD_TOKEN_ATTRIBUTE));
	}

}
