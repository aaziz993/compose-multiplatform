module.exports = {
  resolve: {
    extensions: ['.js', '.ts', '.wasm'],
  },
  module: {
    rules: [
      {
        test: /\.wasm$/,
        type: 'webassembly/experimental',
      },
    ],
  },
};
