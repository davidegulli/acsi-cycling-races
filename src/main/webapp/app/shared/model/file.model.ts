export const enum FileType {
  COVER_IMAGE = 'COVER_IMAGE',
  LOGO_IMAGE = 'LOGO_IMAGE',
  PATH_IMAGE = 'PATH_IMAGE',
  RANKING_PDF = 'RANKING_PDF',
  ATHLETE_ID_DOC = 'ATHLETE_ID_DOC',
  MEDICAL_CERTIFICATION = 'MEDICAL_CERTIFICATION'
}

export interface IFile {
  id?: number;
  title?: string;
  type?: FileType;
  mimeType?: string;
  binaryContentType?: string;
  binary?: any;
  url?: string;
  raceId?: number;
}

export const defaultValue: Readonly<IFile> = {};
