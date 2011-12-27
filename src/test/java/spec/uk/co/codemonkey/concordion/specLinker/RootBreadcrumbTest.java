package spec.uk.co.codemonkey.concordion.specLinker;

import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpec;
import static spec.uk.co.codemonkey.concordion.specLinker.SpecTestHelper.createSpecDirectory;

import java.io.File;
import java.io.IOException;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.co.codemonkey.concordion.specLinker.HtmlDocument;
import uk.co.codemonkey.concordion.specLinker.SpecLinker;

@RunWith(ConcordionRunner.class)
public class RootBreadcrumbTest {

	private File testDir;
	private String specName;
	private Element firstAnchor;

	public void givenASpec(String spec) throws IOException {
		this.specName = spec;
		File dir = createSpecDirectory("RootBreadcrumbTest");
		createSpec(dir, spec);
		testDir = dir;
	}
	
	public void runLinker() throws Exception {
		String path = testDir.getCanonicalPath();
		new SpecLinker().link(testDir.getCanonicalPath(), path);
		
		HtmlDocument htmlDom = new HtmlDocument(new File(testDir, specName));

		Element breadcrumbs = htmlDom.findTagWithAttribute("span", "class", "breadcrumbs");
		firstAnchor = nthElement(breadcrumbs, "a", 1);
		
	}

	private Element nthElement(Element parent, String tagName, int n) {
		NodeList children = parent.getChildNodes();
		int e = 0;
		for(int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				if(child.getNodeName().equals(tagName)) {
					e++;
					if(e == n) {
						return (Element)child;
					}
				}
			}
		}
		return null;
	}
	
	public String linkTarget() {
		return firstAnchor.getAttribute("href");
	}
	
	public String linkName() {
		return firstAnchor.getTextContent();
	}	
	
}
