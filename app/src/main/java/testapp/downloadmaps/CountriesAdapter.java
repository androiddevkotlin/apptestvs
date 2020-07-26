package testapp.downloadmaps;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private List<String> countriesList = new ArrayList<>();
    private static ClickListener clickListener;
    private Context context;

    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_view, viewGroup, false);
        return new CountriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CountriesViewHolder countriesViewHolder, int i) {
        countriesViewHolder.bind(countriesList.get(i));

        context = countriesViewHolder.nameCountry.getContext();
        ParserXml xml = new ParserXml(context);

        final List<String> listCountriesWithRegions = xml.parserXmlForCountriesWithRegions();

        for (int j = 0; j < listCountriesWithRegions.size(); j++) {
            if (countriesViewHolder.nameCountry.getText().toString().equals(listCountriesWithRegions.get(j))) {
                countriesViewHolder.importImageButton.setVisibility(View.GONE);
                countriesViewHolder.removeImageButton.setVisibility(View.GONE);
                countriesViewHolder.downloadProgressBar.setVisibility(View.GONE);
                break;
            } else {
                countriesViewHolder.importImageButton.setVisibility(View.VISIBLE);
                countriesViewHolder.removeImageButton.setVisibility(View.GONE);
            }
        }

        countriesViewHolder.importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                countriesViewHolder.downloadProgressBar.setVisibility(View.VISIBLE);
                countriesViewHolder.importImageButton.setVisibility(View.GONE);
                countriesViewHolder.removeImageButton.setVisibility(View.VISIBLE);
                countriesViewHolder.nameCountry.setPadding(0, 0, 0, 30);

                ParserXml xml = new ParserXml(context);
                final List<String> listCountriesForDownloads = xml.parserXmlForForDownload();

                if (listCountriesForDownloads.contains(countriesViewHolder.nameCountry.getText().toString())) {
                    String url = context.getString(R.string.download_start) + (((RegionActivity) context).getSupportActionBar().getTitle().toString()) + "_"
                            + countriesViewHolder.nameCountry.getText().toString().toLowerCase()
                            + context.getString(R.string.download_europe) + context.getString(R.string.download_end);

                    String fileName = (((RegionActivity) context).getSupportActionBar().getTitle().toString()) + "_"
                            + countriesViewHolder.nameCountry.getText().toString().toLowerCase()
                            + context.getString(R.string.download_europe) + context.getString(R.string.download_end);

                    DownloadTask downloadTask = new DownloadTask(context);
                    downloadTask.execute(url, fileName);
                    Log.d("TAG_URL1", url);

                } else {
                    String url = context.getString(R.string.download_start) + countriesViewHolder.nameCountry.getText().toString()
                            + context.getString(R.string.download_europe) + context.getString(R.string.download_end);
                    String fileName = countriesViewHolder.nameCountry.getText().toString()
                            + context.getString(R.string.download_europe) + context.getString(R.string.download_end);

                    DownloadTask downloadTask = new DownloadTask(context);
                    downloadTask.execute(url, fileName);
                    Log.d("TAG_URL2", url);
                }
            }

            class DownloadTask extends AsyncTask<String, Integer, String> {

                private Context context;

                public DownloadTask(Context context) {
                    this.context = context;
                }


                @Override
                protected String doInBackground(String... strings) {
                    try {
                        URL url = new URL(strings[0]);
                        String pathFolder = Environment.getExternalStorageDirectory() + "/Maps";
                        String pathFile = pathFolder + strings[1];
                        File file = new File(pathFolder);
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        long startTime = System.currentTimeMillis();
                        Log.d("DownloadManager", "download begining");
                        Log.d("DownloadManager", "download url:" + url);
                        Log.d("DownloadManager", "downloaded file name:" + strings[1]);

                        URLConnection ucon = url.openConnection();
                        ucon.connect();

                        int fileLength = ucon.getContentLength();

                        InputStream is = ucon.getInputStream();

                        BufferedInputStream bis = new BufferedInputStream(is);
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                        byte[] data = new byte[4096];
                        long total = 0;
                        int count;
                        int current = 0;

                        while ((current = bis.read(data, 0, data.length)) != -1) {
                            if (isCancelled()) {
                                is.close();
                                return null;
                            }
                            total += current;
                            if (fileLength > 0)
                                publishProgress((int) (total * 100 / fileLength));
                            buffer.write(data, 0, current);

                        }

                        FileOutputStream fos = new FileOutputStream(pathFile);
                        fos.write(buffer.toByteArray());
                        fos.close();
                        Log.d("DownloadManager", "download ready in"
                                + ((System.currentTimeMillis() - startTime) / 1000)
                                + " sec");
                        is.close();

                    } catch (IOException e) {
                        Log.d("DownloadManager", "Error: " + e);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                    countriesViewHolder.downloadProgressBar.setIndeterminate(false);
                    countriesViewHolder.downloadProgressBar.setMax(100);
                    countriesViewHolder.downloadProgressBar.setProgress(values[0]);
                    countriesViewHolder.removeImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancel(true);
                            Log.d("Download", "CANCELED");
                            countriesViewHolder.downloadProgressBar.setVisibility(View.GONE);
                            countriesViewHolder.removeImageButton.setVisibility(View.GONE);
                            countriesViewHolder.importImageButton.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    countriesViewHolder.downloadProgressBar.setVisibility(View.GONE);
                    countriesViewHolder.mapIcon.setColorFilter(context.getResources().getColor(R.color.colorDownloadedMap), PorterDuff.Mode.SRC_IN);
                    countriesViewHolder.importImageButton.setVisibility(View.VISIBLE);
                    countriesViewHolder.removeImageButton.setVisibility(View.GONE);
                    countriesViewHolder.nameCountry.setPadding(0, 0, 0, 0);

                }
            }
        });

        countriesViewHolder.nameCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < listCountriesWithRegions.size(); j++) {
                    if (countriesViewHolder.nameCountry.getText().toString().equals(listCountriesWithRegions.get(j))) {
                        Intent intent = new Intent(context, RegionActivity.class);
                        intent.putExtra("toolbarName", countriesViewHolder.nameCountry.getText().toString());
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    public void setItems(List<String> countries) {
        countriesList.addAll(countries);
        notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return position;
    }

    class CountriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mapIcon;
        private TextView nameCountry;
        private ProgressBar downloadProgressBar;
        private ImageButton importImageButton;
        private ImageButton removeImageButton;

        public CountriesViewHolder(View itemView) {
            super(itemView);

            mapIcon = itemView.findViewById(R.id.iv_item_map);
            nameCountry = itemView.findViewById(R.id.tv_item_country);
            downloadProgressBar = itemView.findViewById(R.id.progress_bar_download);
            importImageButton = itemView.findViewById(R.id.ib_item_import);
            removeImageButton = itemView.findViewById(R.id.ib_item_remove);

        }

        public void bind(String name) {
            nameCountry.setText(name);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
