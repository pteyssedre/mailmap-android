package ca.teyssedre.mailmap;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import ca.teyssedre.mailer.model.MailMessage;

/**
 * A fragment representing a single mail detail screen.
 * This fragment is either contained in a {@link mailListActivity}
 * in two-pane mode (on tablets) or a {@link mailDetailActivity}
 * on handsets.
 */
public class mailDetailFragment extends Fragment {

    private MailMessage data;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public mailDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("data")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            data = (MailMessage) getArguments().getSerializable("data");
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(data.getSubject());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mail_detail, container, false);

        // Show the dummy content as text in a TextView.
        restoreView(data);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            data = (MailMessage) savedInstanceState.getSerializable("data");
            restoreView(data);
        }
    }

    private void restoreView(MailMessage data) {
        if (data != null) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(data.getSubject());
            }
            TextView content = (TextView) rootView.findViewById(R.id.mail_detail);
            WebView html = (WebView) rootView.findViewById(R.id.mail_html);
            switch (data.getType()) {
                case MULTIPART:
                case HTML_TEXT:
                    content.setVisibility(View.GONE);
                    WebSettings settings = html.getSettings();
                    settings.setDefaultTextEncodingName("utf-8");
                    html.loadDataWithBaseURL(null, data.getContentHTML(), "text/html", "utf-8", null);
                    break;
                case PLAIN_TEXT:
                    html.setVisibility(View.GONE);
                    content.setText(data.getContentPlain());
                    content.setVisibility(View.VISIBLE);
            }
        }

    }
}
