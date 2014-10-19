package org.pikater.web.visualisation.implementation.generator;

import java.io.IOException;
import java.io.PrintStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.result.AbstractDSVisResult;
import org.pikater.web.visualisation.implementation.generator.base.ChartGenerator;
import org.pikater.web.visualisation.implementation.renderer.SVGRenderer;

public class SingleSVGGenerator extends SingleGenerator {

	public SingleSVGGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, PrintStream output, int XIndex, int YIndex, int ColorIndex) {
		super(progressListener, dslo, output, XIndex, YIndex, ColorIndex);
		initRenderer();
	}

	public SingleSVGGenerator(AbstractDSVisResult<?, ?> progressListener, JPADataSetLO dslo, PrintStream output, String XName, String YName, String ColorName) {
		super(progressListener, dslo, output, XName, YName, ColorName);
		initRenderer();
	}

	private void initRenderer() {
		this.renderer = new SVGRenderer(output, ChartGenerator.SINGLE_CHART_SIZE, ChartGenerator.SINGLE_CHART_SIZE);
	}

	@Override
	public void create() throws IOException {
		super.create();
		output.close();
	}

}