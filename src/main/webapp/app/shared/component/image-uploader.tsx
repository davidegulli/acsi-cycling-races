import React, { useCallback, useState, useEffect } from 'react';
import { useDropzone } from 'react-dropzone';
import { Row, Col } from 'reactstrap';
import styled from 'styled-components';

export interface IImageUploader {
  id: string;
  onDrop: any;
  previewUrl: string;
  text: string;
}

const thumbsContainer = {};

const thumb = {
  display: 'inline-flex',
  borderRadius: 2,
  border: '1px solid #eaeaea',
  marginBottom: 8,
  marginRight: 8,
  width: '100%',
  height: 150,
  padding: 4
  /*boxSizing: 'border-box'*/
};

const thumbInner = {
  display: 'flex',
  minWidth: '100%',
  overflow: 'hidden'
};

const img = {
  display: 'block',
  width: 'auto',
  height: '100%',
  marginLeft: 'auto',
  marginRight: 'auto'
};

const imageLoadedText = {
  position: 'absolute',
  top: '85%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  color: 'green',
  fontWeight: 700
};

const getColor = props => {
  if (props.isDragAccept) {
    return '#00e676';
  }
  if (props.isDragReject) {
    return '#ff1744';
  }
  if (props.isDragActive) {
    return '#2196f3';
  }
  return '#eeeeee';
};

const Container = styled.div`
  color: #fff !important;
  background-color: #007bff !important;
  border-color: #007bff !important;
  display: inline-block;
  font-weight: 400;
  color: #212529;
  text-align: center;
  vertical-align: middle;
  user-select: none;
  background-color: transparent;
  border: 1px solid transparent;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border-radius: 0.25rem;
`;

const imageUploader = (props: IImageUploader) => {
  const INITAIL_STATE = props.previewUrl != null ? [{ name: '1', preview: props.previewUrl }] : [];

  const [files, setFiles] = useState(INITAIL_STATE);
  const [loaded, setLoaded] = useState(false);

  const onDrop = useCallback(acceptedFiles => {
    // props.onDrop(null, acceptedFiles);
    // setFiles(acceptedFiles.map(file => ({ ...file, preview: URL.createObjectURL(file) })));
  }, []);

  const { getRootProps, getInputProps, isDragActive, isDragAccept, isDragReject } = useDropzone({
    onDrop,
    accept: 'image/*',
    minSize: 100,
    maxSize: 1000000,
    multiple: false,
    noKeyboard: true
  });

  const thumbs =
    files.length > 0 ? (
      files.map(file => (
        <div style={thumbsContainer}>
          <div style={thumb}>
            <div style={thumbInner}>
              <img src={file.preview} style={img} />
            </div>
          </div>
        </div>
      ))
    ) : (
      <div style={thumbsContainer}>
        <div style={thumb}>
          <div style={thumbInner}>
            {loaded ? (
              <>
                <img src="content/images/uploaded-icon.png" style={img} />
              </>
            ) : (
              <img src="content/images/image-placeholder.jpg" style={img} />
            )}
          </div>
        </div>
      </div>
    );

  useEffect(
    () => () => {
      // Make sure to revoke the data uris to avoid memory leaks
      files.forEach(file => URL.revokeObjectURL(file.preview));
    },
    [files]
  );

  const onchange = event => {
    if (event && event.target.files && event.target.files[0]) {
      const uploadedFiles = [...event.target.files];

      props.onDrop(event, uploadedFiles[0]);

      // setFiles(uploadedFiles.map(file => ({ ...file, preview: URL.createObjectURL(file) })));
      setLoaded(true);
    }
  };

  return (
    <div className="container">
      <Row>
        <Col sm="4">{thumbs}</Col>
        <Col>
          <Row>
            <Col>
              <span className="text-form text-muted">{props.text}</span>
            </Col>
          </Row>
          <Row>
            <Col>
              <div className=" mt-3">
                <Container {...getRootProps({ isDragActive, isDragAccept, isDragReject })}>
                  <input id={props.id} {...getInputProps()} onChange={onchange} />
                  <p>Selezione Immagine</p>
                </Container>
              </div>
            </Col>
          </Row>
        </Col>
      </Row>
      <Row>
        <Col>
          <small className="text-form text-muted">Puoi caricare solamente file di tipo jpg o png, con una dimensione massima di 1 MB</small>
        </Col>
      </Row>
    </div>
  );
};

export default imageUploader;

/*
import React, { useCallback, useState, useEffect } from 'react';
import { useDropzone } from 'react-dropzone';
import { Row, Col } from 'reactstrap';
import styled from 'styled-components';

export interface IImageUploader {
  id: string;
  onDrop: any;
  previewUrl: string;
}

const thumbsContainer = {};

const thumb = {
  display: 'inline-flex',
  borderRadius: 2,
  border: '1px solid #eaeaea',
  marginBottom: 8,
  marginRight: 8,
  width: 100,
  height: 100,
  padding: 4
};

const thumbInner = {
  display: 'flex',
  minWidth: 0,
  overflow: 'hidden'
};

const img = {
  display: 'block',
  width: 'auto',
  height: '100%'
};

const getColor = props => {
  if (props.isDragAccept) {
    return '#00e676';
  }
  if (props.isDragReject) {
    return '#ff1744';
  }
  if (props.isDragActive) {
    return '#2196f3';
  }
  return '#eeeeee';
};

const Container = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-width: 2px;
  border-radius: 2px;
  border-color: ${props => getColor(props)};
  border-style: dashed;
  background-color: #fafafa;
  color: #bdbdbd;
  outline: none;
  transition: border 0.24s ease-in-out;
  height: 98px;
  margin: 1px 10px;
`;

const imageUploader = (props: IImageUploader) => {
  const [files, setFiles] = useState([{ name: '1', preview: props.previewUrl }]);

  const onDrop = useCallback(acceptedFiles => {
    props.onDrop(null, acceptedFiles);
    setFiles(acceptedFiles.map(file => ({ ...file, preview: URL.createObjectURL(file) })));
  }, []);

  const { getRootProps, getInputProps, isDragActive, isDragAccept, isDragReject } = useDropzone({
    onDrop,
    accept: 'image/*',
    minSize: 100,
    maxSize: 1000000,
    multiple: false,
    noKeyboard: true
  });

  const thumbs = files.map(file => (
    <Col sm="2" key={file.name}>
      <div style={thumbsContainer}>
        <div style={thumb}>
          <div style={thumbInner}>
            <img src={file.preview} style={img} />
          </div>
        </div>
      </div>
    </Col>
  ));

  useEffect(
    () => () => {
      // Make sure to revoke the data uris to avoid memory leaks
      files.forEach(file => URL.revokeObjectURL(file.preview));
    },
    [files]
  );

  const onchange = event => {
    console.log("onchange");
    if (event && event.target.files && event.target.files[0]) {
      const uploadedFiles = [...event.target.files];

      props.onDrop(event, uploadedFiles[0]);

      setFiles(uploadedFiles.map(file => ({ ...file, preview: URL.createObjectURL(file) })));
    }
  };

  return (
    <div className="container">
      <Row>
        {files[0].preview != null ? thumbs : null}
        <Col>
          <Container {...getRootProps({ isDragActive, isDragAccept, isDragReject })}>
            <input id={props.id} {...getInputProps()} onChange={onchange} />
            <p>Trascina e rilascia qui il file, oppure fai click per selezionare il file</p>
          </Container>
        </Col>
      </Row>
      <Row>
        <Col>
          <small className="text-form text-muted">Puoi caricare solamente file di tipo jpg o png, con una dimensione massima di 1 MB</small>
        </Col>
      </Row>
    </div>
  );
};

export default imageUploader;

*/
