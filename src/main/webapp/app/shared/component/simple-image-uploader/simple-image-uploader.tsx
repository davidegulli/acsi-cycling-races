import React, { Component } from 'react';
import Spinner from './Spinner';
import Images from './Images';
import Buttons from './Buttons';
import WakeUp from './WakeUp';
import './simple-image-uploader.scss';

export default class SimpleImageUploader extends Component {
  state = {
    loading: true,
    uploading: false,
    images: []
  };

  componentDidMount() {
    fetch(`${API_URL}/wake-up`).then(res => {
      if (res.ok) {
        return this.setState({ loading: false });
      }
    });
  }

  onChange = e => {
    const errs = [];
    const files = Array.from(e.target.files);

    const formData = new FormData();
    const types = ['image/png', 'image/jpeg', 'image/gif'];

    files.forEach((file, i) => {
      if (types.every(type => file.type !== type)) {
      }

      if (file.size > 150000) {
      }

      formData.append(i, file);
    });

    this.setState({ uploading: true });

    fetch(`${API_URL}/image-upload`, {
      method: 'POST',
      body: formData
    })
      .then(res => {
        if (!res.ok) {
          throw res;
        }
        return res.json();
      })
      .then(images => {
        this.setState({
          uploading: false,
          images
        });
      })
      .catch(err => {
        err.json().then(e => {
          this.setState({ uploading: false });
        });
      });
  };

  filter = id => this.state.images.filter(image => image.public_id !== id);

  removeImage = id => {
    this.setState({ images: this.filter(id) });
  };

  onError = id => {
    this.setState({ images: this.filter(id) });
  };

  render() {
    const { loading, uploading, images } = this.state;

    const content = () => {
      switch (true) {
        case loading:
          return <WakeUp />;
        case uploading:
          return <Spinner />;
        case images.length > 0:
          return <Images images={images} removeImage={this.removeImage} onError={this.onError} />;
        default:
          return <Buttons onChange={this.onChange} />;
      }
    };

    return (
      <div className="container">
        <div className="buttons">{content()}</div>
      </div>
    );
  }
}
